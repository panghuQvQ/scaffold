package com.wang.scaffold.user.controller;

import com.wang.scaffold.entity.jpa.OperationLog;
import com.wang.scaffold.request.BasePageRequest;
import com.wang.scaffold.response.PageResponse;
import com.wang.scaffold.sharded.helper.JpaPageImpl;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.user.repository.OperationLogRepo;
import com.wang.scaffold.user.request.OperationLogPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/operationLog")
public class OperationLogController {
    @Autowired
    private OperationLogRepo operationLogRepo;

    @PostMapping(path = "/list")
    public PageResponse<OperationLog> operationLogPage(@RequestBody OperationLogPageRequest pageRequest) {
        PageRequest pageReq = PageRequest.of(pageRequest.getPageNum() - 1, pageRequest.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        Page<OperationLog> page = operationLogRepo.findAll((root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            if (StringUtils.hasText(pageRequest.getOperator())) {
                ps.add(cb.equal(root.get("operator").as(String.class), pageRequest.getOperator()));
            }
            if (StringUtils.hasText(pageRequest.getOperation())) {
                ps.add(cb.like(root.get("operation").as(String.class), "%" + pageRequest.getOperation() + "%"));
            }
            if (pageRequest.getStart() != null && pageRequest.getEnd() != null) {
                Date realEnd = new Date(pageRequest.getEnd().getTime() + 24 * 3600 * 1000 - 1);
                ps.add(cb.between(root.get("operateTime").as(Date.class), pageRequest.getStart(), realEnd));
            }
            return cb.and(ps.toArray(new Predicate[0]));
        }, pageReq);
        return PageResponse.success(new JpaPageImpl<OperationLog>(page));
    }

    @PostMapping(path = "/my")
    public PageResponse<OperationLog> myOperationLogPage(@RequestBody BasePageRequest pageRequest) {
        PageRequest pageReq = PageRequest.of(pageRequest.getPageNum() - 1, pageRequest.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        Page<OperationLog> page = operationLogRepo.findAll(
                (root, query, cb) -> cb.equal(root.get("operator").as(String.class), WebAppContextHelper.currentUsername()),
                pageReq);
        return PageResponse.success(new JpaPageImpl<OperationLog>(page));
    }
}
