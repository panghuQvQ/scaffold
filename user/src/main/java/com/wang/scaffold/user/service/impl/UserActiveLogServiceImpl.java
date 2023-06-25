package com.wang.scaffold.user.service.impl;

import com.wang.scaffold.user.entity.UserActiveLog;
import com.wang.scaffold.user.repository.UserActiveLogRepo;
import com.wang.scaffold.user.service.UserActiveLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class UserActiveLogServiceImpl implements UserActiveLogService {

	@Autowired private UserActiveLogRepo userActiveLogRepo;

	/**
	 * 使用异步线程记录用户活跃日志
	 */
	@Async
	@Override
	public void log(String username) {
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Optional<UserActiveLog> op = userActiveLogRepo.findByUsernameAndActiveDate(username, today);
		UserActiveLog log = null;
		if (op.isPresent()) {
			log = op.get();
			log.setTimes(log.getTimes() + 1);
			log.setUpdatedBy(username);
		} else {
			log = new UserActiveLog();
			log.setActiveDate(today);
			log.setTimes(1);
			log.setUsername(username);
			log.setCreatedBy(username);
		}
		userActiveLogRepo.save(log);
	}

}
