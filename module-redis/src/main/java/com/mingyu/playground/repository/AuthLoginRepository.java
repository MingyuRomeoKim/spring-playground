package com.mingyu.playground.repository;

import com.mingyu.playground.entity.AuthLogin;
import org.springframework.data.repository.CrudRepository;

public interface AuthLoginRepository extends CrudRepository<AuthLogin, String>, ArticleHitRepositoryCustom {

}
