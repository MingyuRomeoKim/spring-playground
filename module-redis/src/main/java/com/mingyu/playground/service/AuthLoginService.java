package com.mingyu.playground.service;

import com.mingyu.playground.entity.AuthLogin;
import com.mingyu.playground.repository.AuthLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthLoginService {

    private final AuthLoginRepository authLoginRepository;

    public void login(String accessToken) {
        authLoginRepository.save(AuthLogin.builder().accessToken(accessToken).isLogin(true).build());
    }

    public void logout(String accessToken) {
        authLoginRepository.deleteById(accessToken);
    }

    public boolean isLogin(String accessToken) {
        return authLoginRepository.findById(accessToken).map(AuthLogin::isLogin).orElse(false);
    }
}
