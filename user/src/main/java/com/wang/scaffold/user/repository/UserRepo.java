package com.wang.scaffold.user.repository;

import com.wang.scaffold.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepo extends BaseRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	Optional<User> findByPhone(String phone);

	@EntityGraph(attributePaths = "roles")
	@Query("FROM User e")
	List<User> findAllWithRole();

	@Query("SELECT new User(id, username, name) FROM User WHERE disabled = ?1")
	List<User> findUserByDisabled(boolean disabled);
}
