package com.wang.scaffold.user.repository;


import com.wang.scaffold.user.entity.DeletedUser;

import java.util.Optional;

public interface DeletedUserRepo extends BaseRepository<DeletedUser, Integer> {

    Optional<DeletedUser> findByUsername(String username);

}
