package com.mingyu.playground.services;

import com.mingyu.playground.dto.request.SaveMemberRequestDto;
import com.mingyu.playground.dto.request.UpdateMemberRequestDto;
import com.mingyu.playground.dto.response.FindMemberResponseDto;
import com.mingyu.playground.entities.Member;
import com.mingyu.playground.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * Get All Members
     *
     * @return List<FindMemberResponseDto>
     */
    public List<FindMemberResponseDto> getMembers() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(member -> FindMemberResponseDto.builder()
                        .name(member.getName())
                        .email(member.getEmail())
                        .password(member.getPassword())
                        .phone(member.getPhone())
                        .address(member.getAddress())
                        .role(member.getRole())
                        .build())
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
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return FindMemberResponseDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .phone(member.getPhone())
                .address(member.getAddress())
                .role(member.getRole())
                .build();
    }

    /**
     * Save Member
     *
     * @param saveMemberRequestDto SaveMemberRequestDto
     */
    public void saveMember(SaveMemberRequestDto saveMemberRequestDto) {
        memberRepository.save(saveMemberRequestDto.toEntity());
    }

    /**
     * Delete Member By Email
     *
     * @param email String
     */
    public void deleteMemberByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        memberRepository.delete(member.orElseThrow(() -> new IllegalArgumentException("Member not found")));
    }

    /**
     * Update Member By Email
     *
     * @param email String
     * @param updateMemberRequestDto UpdateMemberRequestDto
     */
    public void updateMemberByEmail(String email, UpdateMemberRequestDto updateMemberRequestDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.update(updateMemberRequestDto);
        memberRepository.save(member);
    }
}
