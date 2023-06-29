package com.wang.scaffold.user.controller;

import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.response.CollectionResponse;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.user.entity.UserFavorite;
import com.wang.scaffold.user.repository.UserFavoriteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class UserFavoriteController {

    @Autowired private UserFavoriteRepo userFavoriteRepo;

    @GetMapping(path = "/mine")
    public BaseResponse<?> myFavorites() {
        String username = WebAppContextHelper.currentUsername();
        List<UserFavorite> items = userFavoriteRepo.findAll((root, query, cb) -> {
            return cb.equal(root.get("username").as(String.class), username);
        }, Sort.by(Sort.Direction.DESC, "createdTime"));
        return CollectionResponse.success(items);
    }

    @PostMapping(path = "/add")
    public BaseResponse<?> addFavorite(@RequestBody UserFavorite userFavorite) {
        String resourceId = userFavorite.getResourceId();
        if(resourceId == null) {
            return BaseResponse.fail("resourceId不能为空");
        }
        String username = WebAppContextHelper.currentUsername();
        userFavoriteRepo.findOne((root, query, cb) -> {
            return cb.and(
                    cb.equal(root.get("username").as(String.class), username),
                    cb.equal(root.get("resourceId").as(String.class), resourceId)
            );
        }).ifPresent(e -> {
            userFavorite.setId(e.getId());
        });
        userFavorite.setUsername(username);
        userFavoriteRepo.save(userFavorite);
        return BaseResponse.success(userFavorite);
    }

    @PostMapping(path = "/remove")
    public BaseResponse<?> removeFavorite(@RequestBody List<Integer> ids) {
        String username = WebAppContextHelper.currentUsername();
        List<UserFavorite> list = userFavoriteRepo.findAll((root, query, cb) -> {
            return cb.and(
                    cb.equal(root.get("username").as(String.class), username),
                    root.get("id").as(Integer.class).in(ids)
            );
        });
        userFavoriteRepo.deleteAll(list);
        return BaseResponse.success();
    }

}
