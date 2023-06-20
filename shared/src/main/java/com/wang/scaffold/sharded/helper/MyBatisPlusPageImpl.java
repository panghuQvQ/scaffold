package com.wang.scaffold.sharded.helper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wang.scaffold.entity.AppPage;

import java.util.List;

/**
 * 分页Page，各个分页实现框架返回的分页最终二次封装至此接口的实现类。
 * <p>以下使用mybatis-plus为例子：
 * <pre>
 */
public class MyBatisPlusPageImpl<T> implements AppPage<T> {

	private static final long serialVersionUID = 1L;

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

    public void setItems(List<T> itmes){
        ipage.setRecords(itmes);
    }
}
/**
 * </pre>
 * =byx
 */
