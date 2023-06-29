package com.wang.scaffold.user.controller;

import com.wang.scaffold.dto.MessageDTO;
import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.user.entity.Message;
import com.wang.scaffold.user.repository.MessageRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title 消息广播与推送
 * @description
 * @author wzy
 * @updateTime 2023/6/29 10:18
 * @throws
 */
@RequestMapping("/notify")
@RestController
public class NotifyController {

	@Autowired SimpMessagingTemplate messagingTemplate;
	@Autowired
	MessageRepo messageRepo;

	/**
	 * 消息广播，发送给所有订阅的用户
	 * @param msg
	 * @return
	 */
	@PostMapping("/news")
	public BaseResponse<String> news(@RequestBody MessageDTO msg) {
		if (MessageDTO.TOPIC.equals(msg.getType())) {
			messagingTemplate.convertAndSend("/topic/news", msg);
			return BaseResponse.success();
		} else {
			return BaseResponse.fail(400, "new消息需要messageType为topic");
		}
	}

	/**
	 * 消息推送，发送给指定的用户
	 * @param msg
	 * @return
	 */
	@PostMapping("/message")
	public BaseResponse<String> message(@RequestBody MessageDTO msg) {
		if (MessageDTO.QUEUE.equals(msg.getType()) && StringUtils.hasText(msg.getMsgTo())) {
			Message message = new Message();
			BeanUtils.copyProperties(msg, message);
			messageRepo.save(message);
			msg.setId(message.getId());
			messagingTemplate.convertAndSendToUser(msg.getMsgTo(), "/queue/msg", msg);
			return BaseResponse.success();
		} else {
			return BaseResponse.fail(400, "message消息需要messageType为queue且msgTo并不能为空");
		}
	}

}
