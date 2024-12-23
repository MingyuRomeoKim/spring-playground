package com.mingyu.playground.v1.service;

import com.mingyu.playground.dto.member.request.SaveMemberRequestDto;
import com.mingyu.playground.dto.member.request.UpdateMemberRequestDto;
import com.mingyu.playground.dto.member.response.FindMemberResponseDto;
import com.mingyu.playground.errors.PlayGroundCommonException;
import com.mingyu.playground.errors.PlayGroundErrorCode;
import com.mingyu.playground.v1.entity.Member;
import com.mingyu.playground.v1.mapper.MemberMapper;
import com.mingyu.playground.v1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Get All Members
     *
     * @return List<FindMemberResponseDto>
     */
    public List<FindMemberResponseDto> getMembers() {
        List<Member> members = memberRepository.findAll();


        return members.stream()
                .map(MemberMapper.INSTANCE::toFindMemberResponseDto)
                .toList();
    }

    /**
     * Get Member By Email
     *
     * @param email String
     * @return FindMemberResponseDto
     */
    public FindMemberResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new PlayGroundCommonException(PlayGroundErrorCode.COMMON_NOT_FOUND.getCode(), PlayGroundErrorCode.COMMON_NOT_FOUND.getMessage()));

        return MemberMapper.INSTANCE.toFindMemberResponseDto(member);
    }

    /**
     * Save Member
     *
     * @param saveMemberRequestDto SaveMemberRequestDto
     */
    public void saveMember(SaveMemberRequestDto saveMemberRequestDto) {

        if (memberRepository.existsByEmail(saveMemberRequestDto.getEmail())) {
            throw new PlayGroundCommonException(PlayGroundErrorCode.COMMON_ALREADY_EXISTS.getCode(), PlayGroundErrorCode.COMMON_ALREADY_EXISTS.getMessage());
        }

        if (memberRepository.existsByPhone(saveMemberRequestDto.getPhone())) {
            throw new PlayGroundCommonException(PlayGroundErrorCode.COMMON_ALREADY_EXISTS.getCode(), PlayGroundErrorCode.COMMON_ALREADY_EXISTS.getMessage());
        }

        saveMemberRequestDto.setPassword(passwordEncoder.encode(saveMemberRequestDto.getPassword()));
        memberRepository.save(saveMemberRequestDto.toEntity());
    }

    /**
     * Delete Member By Email
     *
     * @param email String
     */
    public void deleteMemberByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        memberRepository.delete(member.orElseThrow(() -> new PlayGroundCommonException(PlayGroundErrorCode.COMMON_NOT_FOUND.getCode(), PlayGroundErrorCode.COMMON_NOT_FOUND.getMessage())));
    }

    /**
     * Update Member By Email
     *
     * @param email                  String
     * @param updateMemberRequestDto UpdateMemberRequestDto
     */
    public void updateMemberByEmail(String email, UpdateMemberRequestDto updateMemberRequestDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new PlayGroundCommonException(PlayGroundErrorCode.COMMON_NOT_FOUND.getCode(), PlayGroundErrorCode.COMMON_NOT_FOUND.getMessage()));

        member.update(updateMemberRequestDto);
        memberRepository.save(member);
    }

}
