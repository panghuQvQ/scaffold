package com.wang.scaffold.user.service;

import com.wang.scaffold.user.entity.Message;
import com.wang.scaffold.user.request.MessagePageRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface MessageService {

	int countMsgUnreadByUsername(String username);

	Page<Message> msgPage(String username, MessagePageRequest pageRequest);

	Message readMsg(String username, Integer id);

	void delMsg(String username, Integer id);

	void msgReadAll(String username);

	void msgReadBatch(String username, List<Integer> ids);

    List<Message> msgAfter(String username, Date d);
}
