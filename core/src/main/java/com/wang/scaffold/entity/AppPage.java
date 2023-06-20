package com.wang.scaffold.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页Page，各个分页实现框架返回的分页最终二次封装至此接口的实现类。
 * <p>以下使用mybatis-plus为例子：
 * <pre>{@code
 public class MyBatisPlusPageImpl<T> implements AppPage<T> {

	private IPage<T> ipage;

	public MyBatisPlusPageImpl(IPage<T> ipage) {
		this.ipage = ipage;
	}

	@Override
	public int getPageNum() {
		return (int) ipage.getCurrent();
	}

	@Override
	public int getPageSize() {
		return (int) ipage.getSize();
	}

	@Override
	public long getTotal() {
		return ipage.getTotal();
	}

	@Override
	public int getTotalPages() {
		return (int) ipage.getPages();
	}

	@Override
	public List<T> getItems() {
		return ipage.getRecords();
	}
}
}</pre>
 */
public interface AppPage<T> extends Serializable {

	/** 当前页页码*/
	public int getPageNum();
	/** 每页条数*/
	public int getPageSize();
	/** 总条数*/
	public long getTotal();
	/** 总页数*/
	public int getTotalPages();
	/** 分页对象记录*/
	public List<T> getItems();

}
