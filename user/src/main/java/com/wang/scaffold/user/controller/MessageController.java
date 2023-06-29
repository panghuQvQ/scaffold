package com.wang.scaffold.user.controller;

import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.response.CollectionResponse;
import com.wang.scaffold.response.PageResponse;
import com.wang.scaffold.sharded.helper.JpaPageImpl;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.user.entity.Message;
import com.wang.scaffold.user.request.MessagePageRequest;
import com.wang.scaffold.user.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;

@RequestMapping("/message")
@RestController
public class MessageController {

	@Autowired
	MessageService messageService;

	@PostMapping("/msg")
	public PageResponse<Message> msgPage(@RequestBody MessagePageRequest pageRequest) {
		String username = WebAppContextHelper.currentUsername();
		Page<Message> page = messageService.msgPage(username, pageRequest);
		return PageResponse.success(new JpaPageImpl<Message>(page));
	}

	@GetMapping("/after/{dateTime}")
	public CollectionResponse<Message> msgAfter(@PathVariable String dateTime) {
		String username = WebAppContextHelper.currentUsername();
		TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(dateTime);
		Date d = Date.from(Instant.from(ta));
		List<Message> items = messageService.msgAfter(username, d);
		return CollectionResponse.success(items);
	}

	@GetMapping("/msg")
	public BaseResponse<Message> msg(Integer id) {
		String username = WebAppContextHelper.currentUsername();
		Message msg = messageService.readMsg(username, id);
		return BaseResponse.success(msg);
	}

	@PostMapping("/msg/del")
	public BaseResponse<Message> msgDel(Integer id) {
		String username = WebAppContextHelper.currentUsername();
		messageService.delMsg(username, id);
		return BaseResponse.success();
	}

	@PostMapping("/msg/readAll")
	public BaseResponse<Message> msgReadAll() {
		String username = WebAppContextHelper.currentUsername();
		messageService.msgReadAll(username);
		return BaseResponse.success();
	}

	@PostMapping("/msg/readBatch")
	public BaseResponse<Message> msgReadBatch(@RequestBody List<Integer> ids) {
		String username = WebAppContextHelper.currentUsername();
		messageService.msgReadBatch(username, ids);
		return BaseResponse.success();
	}

	@GetMapping("/msgUnread")
	public BaseResponse<Integer> msgUnread() {
		String username = WebAppContextHelper.currentUsername();
		Integer count = messageService.countMsgUnreadByUsername(username);
		return BaseResponse.success(count);
	}
}
