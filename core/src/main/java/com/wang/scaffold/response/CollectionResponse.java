package com.wang.scaffold.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.ToString;

import java.util.Collection;

import static com.wang.scaffold.consts.WebConstants.SECCUSS_OK_CODE;
import static com.wang.scaffold.consts.WebConstants.SECCUSS_OK_MSG;


/**
 *
 * @author zhou wei
 *
 */
@ToString(callSuper = true)
@JsonInclude(Include.NON_NULL)
public class CollectionResponse<T> extends BaseResponse<T> {

	private static final long serialVersionUID = 1L;

	private Collection<T> items;

	public CollectionResponse(){}

	public CollectionResponse(boolean success, int code, Collection<T> items) {
		this(success, code, null, items);
	}

	public CollectionResponse(boolean success, int code, String message, Collection<T> items) {
		super(success, code, message);
		this.items = items;
	}

	public static <T> CollectionResponse<T> success(Collection<T> items) {
		return new CollectionResponse<T>(true, SECCUSS_OK_CODE, SECCUSS_OK_MSG, items);
	}

	public static <T> CollectionResponse<T> success(String message, Collection<T> items) {
		return new CollectionResponse<T>(true, SECCUSS_OK_CODE, message, items);
	}

	public Collection<T> getItems() {
		return items;
	}

	public void setItems(Collection<T> items) {
		this.items = items;
	}

	public void add(T t) {
		if (this.items != null) {
			this.items.add(t);
		}
	}

	public void addAll(Collection<T> anotherCollection) {
		if (this.items != null) {
			this.items.addAll(anotherCollection);
		}
	}

	public boolean remove(T t) {
		if (this.items != null) {
			return this.items.remove(t);
		}
		return false;
	}

	public boolean remove(Collection<T> collection) {
		if (this.items != null) {
			return this.items.removeAll(collection);
		}
		return false;
	}
}
