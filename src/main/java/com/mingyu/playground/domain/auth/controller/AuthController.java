package com.mingyu.playground.domain.auth.controller;

import com.mingyu.playground.common.response.PlayGroundResponse;
import com.mingyu.playground.domain.auth.dto.request.AuthLoginRequestDto;
import com.mingyu.playground.domain.auth.dto.response.AuthLoginResponseDto;
import com.mingyu.playground.domain.auth.service.AuthService;
import com.mingyu.playground.domain.jwt.service.TokenService;
import com.mingyu.playground.domain.jwt.util.JwtTokenizer;
import com.mingyu.playground.domain.member.dto.response.FindMemberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Tag(name = "Auth", description = "Auth 관련 Controller 입니다. 로그인 및 로그아웃, 회원가입을 담당합니다.")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @Operation(summary = "로그인", description = "회원 아이디와 비밀번호를 가지고 로그인 후 access 토큰을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FindMemberResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Description("Login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthLoginRequestDto authLoginRequestDto, HttpServletResponse httpServletResponse) {
        // 인증처리
        AuthLoginResponseDto authLoginResponseDto = authService.login(authLoginRequestDto);

        // Token 쿠키 저장
        Cookie accessTokenCookie = new Cookie("accessToken", authLoginResponseDto.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.accessTokenExpire / 1000));
        httpServletResponse.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", authLoginResponseDto.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.refreshTokenExpire / 1000));
        httpServletResponse.addCookie(refreshTokenCookie);

        return PlayGroundResponse.build(authLoginResponseDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String accessToken = null;

        // access / refresh Token cookie 삭제
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "accessToken":
                        accessToken = cookie.getValue();
                    case "refreshToken":
                        cookie.setValue("");
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        httpServletResponse.addCookie(cookie);
                        break;
                }
            }
        }

        // tokens 데이터 삭제
        tokenService.deleteByAccessToken(accessToken);
        return PlayGroundResponse.ok();
    }
}
