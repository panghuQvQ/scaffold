package com.wang.scaffold.user.auth.interceptor;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.wang.scaffold.sharded.exception.CaptchaException;
import com.wang.scaffold.user.auth.details.CaptchaAuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * 校验验证码拦截器：此处为 后端二次校验
 *
 * 第一次校验： /captcha/check 方法，在底层 CaptchaController 类中实现，用户拖动/点击一次验证码拼图即视为一次“验证”，不论拼图/点击是否正确
 * @author weizhou
 */
@Component
public class CaptchaInterceptor implements PreAuthenInterceptor {

	@Autowired private CaptchaService captchaService;

	@Override
	public void preAuthenticate(Authentication authenToken) {
		Object details = authenToken.getDetails();
		if (details instanceof CaptchaAuthenticationDetails) {
			CaptchaAuthenticationDetails captchaAuthenticationDetails = (CaptchaAuthenticationDetails) details;
			CaptchaVO captchaVO = captchaAuthenticationDetails.getCaptchaVO();
			ResponseModel responseModel = captchaService.verification(captchaVO); // 二次校验图片验证码
			if (!responseModel.isSuccess()) {
				//验证码校验失败，返回信息告诉前端
				//repCode  0000  无异常，代表成功
				//repCode  9999  服务器内部异常
				//repCode  0011  参数不能为空
				//repCode  6110  验证码已失效，请重新获取
				//repCode  6111  验证失败
				//repCode  6112  获取验证码失败,请联系管理员
				throw new CaptchaException(responseModel.getRepMsg() + "(" + responseModel.getRepCode() + ")");
			}
		}
	}

}
