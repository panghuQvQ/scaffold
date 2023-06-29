package com.wang.scaffold.user.controller;

import com.wang.scaffold.annotation.SysLog;
import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.user.entity.UserPreference;
import com.wang.scaffold.user.repository.UserPreferenceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/user-preference")
@RestController
public class UserPreferenceController {

	@Autowired private UserPreferenceRepo userPreferenceRepo;

	/**
	 * 获取某人的某项用户偏好
	 * @param username
	 * @param key
	 * @return
	 */
	@GetMapping
	public BaseResponse<?> getUserPreference(String username, String key) {
		Optional<UserPreference> op = userPreferenceRepo.findByUsernameAndPreferenceKey(username, key);
		return BaseResponse.success("OK", op.isPresent()? op.get().getPreferenceValue() : "");
	}

	/**
	 * 按key获取所有人的用户偏好
	 * @param key
	 * @return
	 */
	@GetMapping("/all")
	public BaseResponse<?> getAllUserPreference(String key) {
		List<UserPreference> list = userPreferenceRepo.findByPreferenceKey(key);
		Map<String, String> preferenceMap = list.stream().collect(Collectors.toMap(UserPreference::getUsername, UserPreference::getPreferenceValue));
		return BaseResponse.success(preferenceMap);
	}

	/**
	 * 按key获取一些人的用户偏好
	 * @param key
	 * @param usernames
	 * @return
	 */
	@PostMapping("/some")
	public BaseResponse<?> getSomeUserPreference(String key, @RequestBody List<String> usernames) {
		List<UserPreference> list = userPreferenceRepo.findByPreferenceKey(key);
		Map<String, String> preferenceMap = list.stream()
				.filter(e -> usernames.contains(e.getUsername()))
				.collect(Collectors.toMap(UserPreference::getUsername, UserPreference::getPreferenceValue));
		return BaseResponse.success(preferenceMap);
	}

	/**
	 * 获取我的用户偏好
	 * @return
	 */
	@GetMapping("/u")
	public BaseResponse<?> getUserPreferences() {
		String username = WebAppContextHelper.currentUsername();
		List<UserPreference> list = userPreferenceRepo.findByUsername(username);
		Map<String, String> preferenceMap = list.stream().collect(Collectors.toMap(UserPreference::getPreferenceKey, UserPreference::getPreferenceValue));
		return BaseResponse.success(preferenceMap);
	}

	/**
	 * 设置我的用户偏好
	 * @param preferences
	 * @return
	 */
	@SysLog(module = "用户偏好", operation = "设置我的")
	@PostMapping("/u")
	public BaseResponse<?> setUserPreferences(@RequestBody Map<String, Object> preferences) {
		String username = WebAppContextHelper.currentUsername();
		this._setUserPreferences(username, preferences);
		return BaseResponse.success();
	}

	/**
	 * 按用户名获取用户偏好
	 * @param username
	 * @return
	 */
	@GetMapping("/u/{username}")
	public BaseResponse<?> getUserPreferences(@PathVariable String username) {
		List<UserPreference> list = userPreferenceRepo.findByUsername(username);
		Map<String, String> preferenceMap = list.stream().collect(Collectors.toMap(UserPreference::getPreferenceKey, UserPreference::getPreferenceValue));
		return BaseResponse.success(preferenceMap);
	}

	/**
	 * 按用户名设置用户偏好
	 * @param username
	 * @param preferences
	 * @return
	 */
	@SysLog(module = "用户偏好", operation = "设置XX的")
	@PostMapping("/u/{username}")
	public BaseResponse<?> setUserPreferences(@PathVariable String username, @RequestBody Map<String, Object> preferences) {
		this._setUserPreferences(username, preferences);
		return BaseResponse.success();
	}

	private void _setUserPreferences(String username, Map<String, Object> preferences) {
		List<UserPreference> list = userPreferenceRepo.findByUsername(username);
		Map<String, UserPreference> temp = list.stream().collect(Collectors.toMap(UserPreference::getPreferenceKey, e -> e));
		preferences.forEach((k, v) -> {
			UserPreference pref = temp.get(k);
			if (pref != null) {
				pref.setPreferenceValue(v.toString());
			} else {
				pref = new UserPreference();
				pref.setUsername(username);
				pref.setPreferenceKey(k);
				pref.setPreferenceValue(v.toString());
				list.add(pref);
			}
		});
		userPreferenceRepo.saveAll(list);
	}
}
