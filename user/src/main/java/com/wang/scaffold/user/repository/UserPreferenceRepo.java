package com.wang.scaffold.user.repository;


import com.wang.scaffold.user.entity.UserPreference;

import java.util.List;
import java.util.Optional;

public interface UserPreferenceRepo extends BaseRepository<UserPreference, Integer> {

	Optional<UserPreference> findByUsernameAndPreferenceKey(String username, String preferenceKey);

	List<UserPreference> findByPreferenceKey(String preferenceKey);

	List<UserPreference> findByUsername(String username);

}
