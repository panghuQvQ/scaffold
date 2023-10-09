package com.wang.scaffold.files.controller;

import com.wang.scaffold.file.support.SimpleUploadFileEntity;
import com.wang.scaffold.files.consts.RedisKeyConsts;
import com.wang.scaffold.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persist")
public class PersistController {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@PostMapping(path = "/merge")
	public BaseResponse<?> merge(@RequestBody List<SimpleUploadFileEntity> request) {
		List<String> urls = new ArrayList<>();
		request.forEach(e -> {
			if (!CollectionUtils.isEmpty(e.getFileUrls()))
				urls.addAll(e.getFileUrls());
		});
		this.claimDangling(urls);

		request.forEach(e -> {
			List<String> dbItems = this.findByFileEntityIdInRedis(e.getFileEntityId());
			// 如果redis里存过不为空，那取出的url们不在新传来的url中，就需要将他们放入dangling中了。
			if (!CollectionUtils.isEmpty(dbItems)) {
				List<String> toDangling = dbItems.stream().filter(i -> e.getFileUrls() == null || !e.getFileUrls().contains(i))
						.collect(Collectors.toList());
				this.setDangling(toDangling);
			}
			this.setFileEntityIdInRedis(e.getFileEntityId(), e.getFileUrls());
		});
		return BaseResponse.success();
	}

	@PostMapping(path = "/remove")
	public BaseResponse<?> remove(@RequestBody List<SimpleUploadFileEntity> request) {
		request.forEach(e -> {
			List<String> dbItems = this.findByFileEntityIdInRedis(e.getFileEntityId());
			if (!CollectionUtils.isEmpty(dbItems)) {
				this.setDangling(dbItems);
			}
			String key = RedisKeyConsts.list_file_entity_prefix + e.getFileEntityId();
			redisTemplate.delete(key);
		});
		return BaseResponse.success();
	}

	private void claimDangling(List<String> urls) {
		if (CollectionUtils.isEmpty(urls)) {
			return;
		}
		BoundZSetOperations<String, String> ops = redisTemplate.boundZSetOps(RedisKeyConsts.zset_dangling);
		ops.remove(urls.toArray());
	}

	private void setDangling(List<String> urls) {
		BoundZSetOperations<String, String> ops = redisTemplate.boundZSetOps(RedisKeyConsts.zset_dangling);
		urls.forEach(e -> {
			ops.add(e, System.currentTimeMillis());
		});

	}

	private List<String> findByFileEntityIdInRedis(String fileEntityId) {
		ListOperations<String, String> ops = redisTemplate.opsForList();
		List<String> result = ops.range(RedisKeyConsts.list_file_entity_prefix + fileEntityId, 0, -1);
		return result;
	}

	private void setFileEntityIdInRedis(String fileEntityId, List<String> urls) {
		String key = RedisKeyConsts.list_file_entity_prefix + fileEntityId;
		redisTemplate.delete(key);
		if (!CollectionUtils.isEmpty(urls)) {
			redisTemplate.opsForList().leftPushAll(key, urls);
		}
	}
}
