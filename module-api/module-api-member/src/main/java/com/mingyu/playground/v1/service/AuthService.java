package com.mingyu.playground.v1.service;


import com.mingyu.playground.dto.auth.request.AuthLoginRequestDto;
import com.mingyu.playground.dto.auth.request.AuthSignUpRequestDto;
import com.mingyu.playground.dto.auth.response.AuthLoginResponseDto;
import com.mingyu.playground.entity.member.Member;
import com.mingyu.playground.entity.member.Token;
import com.mingyu.playground.errors.PlayGroundCommonException;
import com.mingyu.playground.errors.PlayGroundErrorCode;
import com.mingyu.playground.v1.jwt.util.JwtTokenizer;
import com.mingyu.playground.v1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
        // 인증 처리
        Member member = this.checkAuthentication(authLoginRequestDto);
        // 토큰 생성 및 저장
        Token token = this.getToken(member);

        return AuthLoginResponseDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

    /**
     * 인증 처리
     * @param authLoginRequestDto AuthLoginRequestDto
     * @return Member
     */
    private Member checkAuthentication(AuthLoginRequestDto authLoginRequestDto) {
        Member member = memberRepository.findByEmail(authLoginRequestDto.getEmail())
                .orElseThrow(() -> new PlayGroundCommonException(PlayGroundErrorCode.AUTH_INVALID.getCode(), PlayGroundErrorCode.AUTH_INVALID.getMessage()));

        if (member == null) {
            throw new PlayGroundCommonException(PlayGroundErrorCode.AUTH_INVALID.getCode(), PlayGroundErrorCode.AUTH_INVALID.getMessage());
        }

        // 비밀번호 일치 여부 체크
        if (!passwordEncoder.matches(authLoginRequestDto.getPassword(), member.getPassword())) {
            throw new PlayGroundCommonException(PlayGroundErrorCode.AUTH_PASSWORD_MISMATCH.getCode(), PlayGroundErrorCode.AUTH_PASSWORD_MISMATCH.getMessage());
        }

        return member;
    }

    /**
     * 토큰 발급
     * @param member Member
     * @return Token
     */
    private Token getToken (Member member) {

        // 토큰 발급
        String accessToken = jwtTokenizer.createAccessToken(member.getId(), member.getEmail(), member.getName(), member.getRole());
        String refreshToken = jwtTokenizer.createRefreshToken(member.getId(), member.getEmail(), member.getName(), member.getRole());

        // 토큰 저장
        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .grantType("Bearer")
                .build();
        tokenService.saveOrUpdate(token);

        return token;
    }

    /**
     * 회원가입
     * @param authSignUpRequestDto AuthSignUpRequestDto
     */
    public void signup(AuthSignUpRequestDto authSignUpRequestDto) {
        if (memberRepository.existsByEmail(authSignUpRequestDto.getEmail())) {
            throw new PlayGroundCommonException(PlayGroundErrorCode.COMMON_ALREADY_EXISTS.getCode(), PlayGroundErrorCode.COMMON_ALREADY_EXISTS.getMessage());
        }

        if (memberRepository.existsByPhone(authSignUpRequestDto.getPhone())) {
            throw new PlayGroundCommonException(PlayGroundErrorCode.COMMON_ALREADY_EXISTS.getCode(), PlayGroundErrorCode.COMMON_ALREADY_EXISTS.getMessage());
        }

        authSignUpRequestDto.setPassword(passwordEncoder.encode(authSignUpRequestDto.getPassword()));
        memberRepository.save(authSignUpRequestDto.toEntity());
    }
}
