package com.wang.scaffold.user.service.impl;

import com.wang.scaffold.user.entity.Message;
import com.wang.scaffold.user.repository.MessageRepo;
import com.wang.scaffold.user.request.MessagePageRequest;
import com.wang.scaffold.user.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepo messageRepo;

    @Override
    public int countMsgUnreadByUsername(String username) {
        long count = messageRepo.count((root, query, cb) -> {
            return cb.and(
                    cb.equal(root.get("msgTo").as(String.class), username),
                    cb.equal(root.get("readStatus").as(boolean.class), false)
            );
        });
        return (int) count;
    }

    @Override
    public Page<Message> msgPage(String username, MessagePageRequest pageRequest) {
        PageRequest pageReq = PageRequest.of(pageRequest.getPageNum() - 1, pageRequest.getPageSize(), Sort.by(Direction.DESC, "id"));
        Page<Message> page = messageRepo.findAll((root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            if (pageRequest.getStatus() != null) {
                ps.add(cb.equal(root.get("readStatus").as(boolean.class), pageRequest.getStatus()));
            }
            ps.add(cb.equal(root.get("msgTo").as(String.class), username));
            return cb.and(ps.toArray(new Predicate[0]));
        }, pageReq);
        return page;
    }

    @Transactional
    @Override
    public Message readMsg(String username, Integer id) {
    	Message msg = null;
        Optional<Message> msgOpt = messageRepo.findById(id);
        if (msgOpt.isPresent() && msgOpt.get().getMsgTo().equals(username)) {
        	msg = msgOpt.get();
            msg.setReadStatus(true);
            messageRepo.save(msg);
        }
        return msg;
    }

    @Transactional
    @Override
    public void delMsg(String username, Integer id) {
        messageRepo.findById(id).ifPresent(msg -> {
            if (msg.getMsgTo().equals(username)) {
                messageRepo.delete(msg);
            }
        });
    }

    @Transactional
    @Override
    public void msgReadAll(String username) {
        messageRepo.readAll(username);
    }

    @Transactional
    @Override
    public void msgReadBatch(String username, List<Integer> ids) {
        messageRepo.findAllById(ids).stream().filter(e -> {
            return username.equals(e.getMsgTo());
        }).filter(e -> {
            return !e.isReadStatus();
        }).forEach(e -> {
            e.setReadStatus(true);
            messageRepo.save(e);
        });
    }

    @Override
    public List<Message> msgAfter(String username, Date d) {
        List<Message> result = messageRepo.findAll((root, query, cb) -> {
            return cb.and(
                    cb.greaterThan(root.get("createdTime").as(Date.class), d),
                    cb.equal(root.get("msgTo").as(String.class), username)
            );
        }, Sort.by("id").ascending());
        return result;
    }
}
