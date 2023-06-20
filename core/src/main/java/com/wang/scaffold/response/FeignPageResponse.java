package com.wang.scaffold.response;

import lombok.Data;

import java.util.List;

/**
 * @author twgu
 * @date 2020/6/30
 * @desc
 */
@Data
public class FeignPageResponse<T> {
    private List<T> items;

    private boolean success;

    private int code;

    private String message;

    private int pageNum;

    private int pageSize;

    private long total;

    private int totalPages;
}
