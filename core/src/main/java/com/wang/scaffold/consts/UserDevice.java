package com.wang.scaffold.consts;

/**
 * 用户使用设备类型
 */
public class UserDevice {

	private DeviceType deviceType;
	private String deviceName;

	public UserDevice() { }
	public UserDevice(DeviceType deviceType, String deviceName) {
		this.deviceType = deviceType;
		this.deviceName = deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public DeviceType getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public enum DeviceType {
		BROWSER, IOS, ANDROID, UNKNOWN;
	}
}
