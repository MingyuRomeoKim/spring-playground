package com.mingyu.playground.mapper.member;


import com.mingyu.playground.dto.member.response.FindMemberResponseDto;
import com.mingyu.playground.entity.member.Member;

public class MemberMapperImpl implements MemberMapper{

    @Override
    public FindMemberResponseDto toFindMemberResponseDto(Member member) {
        if ( member == null ) {
            return null;
        }

        FindMemberResponseDto.FindMemberResponseDtoBuilder findMemberResponseDto = FindMemberResponseDto.builder();
        findMemberResponseDto.name( member.getName() );
        findMemberResponseDto.email( member.getEmail() );
        findMemberResponseDto.password( member.getPassword() );
        findMemberResponseDto.phone( member.getPhone() );
        findMemberResponseDto.address( member.getAddress() );
        findMemberResponseDto.role( member.getRole().name() );

        return findMemberResponseDto.build();
    }
}
