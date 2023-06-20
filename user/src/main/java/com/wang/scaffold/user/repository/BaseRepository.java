package com.wang.scaffold.user.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T,ID> extends JpaRepositoryImplementation<T, ID> {

}
