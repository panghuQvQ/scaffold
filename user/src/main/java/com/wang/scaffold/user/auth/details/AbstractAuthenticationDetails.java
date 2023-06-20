package com.wang.scaffold.user.auth.details;

import com.wang.scaffold.consts.UserDevice;
import com.wang.scaffold.consts.UserDevice.DeviceType;
import com.wang.scaffold.sharded.security.ClientInfoUtil;
import com.wang.scaffold.sharded.user.ClientInfo;
import com.wang.scaffold.utils.userAgent.UserAgentUtils;
import eu.bitwalker.useragentutils.Browser;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractAuthenticationDetails {

	/** 客户端信息 */
	private ClientInfo clientInfo;

	public AbstractAuthenticationDetails(HttpServletRequest request) {
		clientInfo = ClientInfoUtil.extraClientInfo(request);
		String device = request.getHeader("x-login-device");
		if (device != null) {
			for (DeviceType deviceType : DeviceType.class.getEnumConstants()) {
				if(device.startsWith(deviceType.name())) {
					clientInfo.setDevice(new UserDevice(deviceType, device));
					break;
				}
			}
		}
        if(clientInfo.getDevice() == null) {
			if(clientInfo.isApp()) throw new IllegalArgumentException();

        	Browser b = UserAgentUtils.getUserAgent(request).getBrowser();
			clientInfo.setDevice(new UserDevice(DeviceType.BROWSER, b.getName()));
        }
	}

	public ClientInfo getClientInfo() {
		return clientInfo;
	}
}
