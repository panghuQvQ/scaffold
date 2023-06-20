package com.wang.scaffold.consts;

public abstract class WebConstants {

	// 单页数据大小 默认10
	public static final int DEFAULT_PAGE_SIZE = 8;

	// 页码 默认1
	public static final int DEFAULT_PAGE_NUM = 1;

	// 成功 200 ok
	public static final int SECCUSS_OK_CODE = 200;
	public static final String SECCUSS_OK_MSG = "OK";

	// 失败 500 服务器内部错误
	public static final int FAIL_INTERNAL_CODE = 500;
	public static final String FAIL_INTERNAL_MSG = "服务器内部错误";

	/** token过期 */
	public static final int TOKEN_EXPIRE = 4001;
	/** 验证码异常 */
	public static final int CAPTCHA_EX = 4006;
	/** app版本过旧 */
	public static final int APP_OUTDATED = 4009;
	/** 密码错误 */
	public static final int WRONG_PASSWORD = 4011;
	/** 账号停用 */
	public static final int ACCOUNT_DISABLED = 4012;
	/** 未找到手机号绑定的账户 */
	public static final int PHONE_NOT_FOUND = 4040;
	/** 登录过期 */
	public static final int LOGIN_EXPIRE = 9001;
	/** 账号被锁 */
	public static final int ACCOUNT_LOCKED = 9000;
	/** 密钥泄露，账号被攻击等 */
	public static final int UNDER_ATTACT = 9110;

	/** 报餐超时 */
	public static  final int LIMIT_TIME_OUT = 5555;
}
