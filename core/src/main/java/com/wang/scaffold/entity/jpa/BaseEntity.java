package com.wang.scaffold.entity.jpa;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Based on Spring data jpa.
 * @author zhou wei
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// 创建人
	@CreatedBy
	@Column(length = 50)
	private String createdBy;
	// 创建时间
	@CreatedDate
	private Date createdTime;
	// 更新人
	@LastModifiedBy
	@Column(length = 50)
	private String updatedBy;
	// 更新时间
	@LastModifiedDate
	private Date updatedTime;
}
