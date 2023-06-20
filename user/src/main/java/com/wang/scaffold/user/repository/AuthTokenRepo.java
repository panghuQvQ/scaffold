package com.wang.scaffold.user.repository;

import com.wang.scaffold.user.entity.AuthToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;


public interface AuthTokenRepo extends BaseRepository<AuthToken, Integer> {

	@Query("SELECT e FROM AuthToken e WHERE e.refreshToken = ?1 AND e.expireTime > ?2")
	AuthToken findByRefreshToken(String refreshToken, Date date);

	@Modifying
	@Query("DELETE FROM AuthToken e WHERE e.username = ?1 AND e.expireTime < ?2")
	void deleteExpiredToken(String username, Date date);

}
