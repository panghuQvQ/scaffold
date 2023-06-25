package com.wang.scaffold.user.auth.details;


import com.anji.captcha.model.vo.CaptchaVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @title CaptchaVO --->spring-boot-starter-captcha 验证码 依赖
 * @description
 * @author wzy
 * @updateTime 2023/6/20 16:18
 * @throws
 */
public class CaptchaAuthenticationDetails extends AbstractAuthenticationDetails {

	private CaptchaVO captchaVO = new CaptchaVO();

	public CaptchaAuthenticationDetails(HttpServletRequest request) {
		super(request);

		String captchaVerification = request.getParameter("captchaVerification");
		captchaVO.setCaptchaVerification(captchaVerification); // 设置图片验证码的验证信息
	}

	public CaptchaVO getCaptchaVO() {
		return captchaVO;
	}
}
