package com.wang.scaffold.entity;

import com.wang.scaffold.annotation.SysLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 操作日志表
 * @author zhou wei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OperationLog {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	/** 操作，由{@link SysLog} 定义 */
	private String operation;

	/** 请求方法eg:XXController.method */
	private String requestMethod;

	/** 方法入参 */
	private String parameters;

	/** 返回值 */
	private String response;

	/** 操作时间 */
	private Date operateTime;

	/** 操作人 */
	private String operator;

	private String ip;

	private String userAgent;
}
