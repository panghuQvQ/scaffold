package com.wang.scaffold.user.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wang.scaffold.request.BasePageRequest;
import lombok.Data;

import java.util.Date;

@Data
public class OperationLogPageRequest extends BasePageRequest {
    /** 操作人 */
    private String operator;
    /** 操作内容 */
    private String operation;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date start;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date end;
}
