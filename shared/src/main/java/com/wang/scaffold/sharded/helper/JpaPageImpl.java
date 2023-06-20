package com.wang.scaffold.sharded.helper;

import com.wang.scaffold.entity.AppPage;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 	封裝jpa分页查询
 * @author gu ping
 *
 * @param <T>
 */
@Data
public class JpaPageImpl<T> implements AppPage<T> {

	private static final long serialVersionUID = 1L;
	private Page<T> springPage;

    public JpaPageImpl(){
    }

    public JpaPageImpl(Page<T> jpaPage){
        this.springPage = jpaPage;
    }

    @Override
    public int getPageNum() {
    	if (springPage == null) return 0;
        return springPage.getNumber() + 1;
    }

    @Override
    public int getPageSize() {
    	if (springPage == null) return 0;
        return springPage.getSize();
    }

    @Override
    public long getTotal() {
    	if (springPage == null) return 0;
        return  springPage.getTotalElements();
    }

    @Override
    public int getTotalPages() {
    	if (springPage == null) return 0;
        return springPage.getTotalPages();
    }

    @Override
    public List<T> getItems() {
        return springPage.getContent();
    }

}
