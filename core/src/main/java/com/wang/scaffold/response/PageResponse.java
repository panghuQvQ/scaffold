package com.wang.scaffold.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wang.scaffold.entity.AppPage;
import lombok.ToString;

import java.util.List;

import static com.wang.scaffold.consts.WebConstants.SECCUSS_OK_CODE;
import static com.wang.scaffold.consts.WebConstants.SECCUSS_OK_MSG;


@ToString(callSuper = true)
@JsonInclude(Include.NON_NULL)
public class PageResponse<T> extends BaseResponse<T> {

	private static final long serialVersionUID = 1L;

	private AppPage<T> page;

	public PageResponse(){
		super();
	}

	public PageResponse(boolean success, int code, AppPage<T> page) {
		this(success, code, null, page);
	}

	public PageResponse(boolean success, int code, String message, AppPage<T> page) {
		super(success, code, message);
		this.page = page;
	}

	public static <T> PageResponse<T> success(AppPage<T> page) {
		return new PageResponse<T>(true, SECCUSS_OK_CODE, SECCUSS_OK_MSG, page);
	}

	public static <T> PageResponse<T> success(String message, AppPage<T> page) {
		return new PageResponse<T>(true, SECCUSS_OK_CODE, message, page);
	}

	public int getPageNum() {
		return page.getPageNum();
	}

	public int getPageSize() {
		return page.getPageSize();
	}

	public long getTotal() {
		return page.getTotal();
	}

	public int getTotalPages() {
		return page.getTotalPages();
	}

	public List<T> getItems() {
		return page.getItems();
	}

	public void setPage(AppPage<T> page) {
		this.page = page;
	}
}
