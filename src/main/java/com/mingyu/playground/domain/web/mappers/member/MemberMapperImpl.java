package com.mingyu.playground.domain.web.mappers.member;

import com.mingyu.playground.domain.web.v1.member.dto.response.FindMemberResponseDto;
import com.mingyu.playground.domain.web.v1.member.entities.Member;

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
