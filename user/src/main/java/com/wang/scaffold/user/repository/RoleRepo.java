package com.wang.scaffold.user.repository;

import com.wang.scaffold.user.entity.Role;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RoleRepo extends BaseRepository<Role, Integer> {

	@Query("SELECT e FROM Role e WHERE e.name = ?1")
	Role findByRoleName(String roleName);

	@Query("FROM Role e JOIN FETCH e.permissions")
	List<Role> findAllWithPermissions();
}
