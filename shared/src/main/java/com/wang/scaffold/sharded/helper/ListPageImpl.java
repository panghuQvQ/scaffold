package com.wang.scaffold.sharded.helper;


import com.wang.scaffold.entity.AppPage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 对集合进行分页返回处理
 *
 * @author gu ping
 *
 * @param <T>
 */
public class ListPageImpl<T> implements AppPage<T> {
	private static final long serialVersionUID = 1L;

	private int pageNum;

	private int pageSize;

	private List<T> list;

	public ListPageImpl(List<T> list, int pageNum, int pageSize) {
		this.list = list;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	@Override
	public int getPageNum() {
		return this.pageNum;
	}

	@Override
	public int getPageSize() {
		return this.pageSize;
	}

	@Override
	public long getTotal() {
		return this.list.size();
	}

	@Override
	public int getTotalPages() {
		int remainder = ((int) this.getTotal() % this.getPageSize());
		if (remainder > 0) {
			return (((int) this.getTotal() / this.getPageSize()) + 1);
		} else {
			return (int) this.getTotal() / this.getPageSize();
		}

	}

	@Override
	public List<T> getItems() {
		List<T> collect = this.list.stream().skip((this.pageNum - 1) * this.pageSize).limit(pageSize)
				.collect(Collectors.toList());
		return collect;
	}

}
