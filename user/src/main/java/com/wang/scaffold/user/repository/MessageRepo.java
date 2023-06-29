package com.wang.scaffold.user.repository;

import com.wang.scaffold.user.entity.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MessageRepo extends BaseRepository<Message, Integer> {

	List<Message> findByMsgToAndReadStatus(String username, boolean readStatus);

	@Modifying
	@Query("UPDATE Message SET readStatus = 1 WHERE msgTo = ?1")
	void readAll(String msgTo);

}
