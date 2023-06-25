package com.wang.scaffold.user.repository;


import com.wang.scaffold.user.entity.UserActiveLog;

import java.util.Optional;

public interface UserActiveLogRepo extends BaseRepository<UserActiveLog, Integer> {

	Optional<UserActiveLog> findByUsernameAndActiveDate(String username, String today);

}
