package com.mingyu.playground.domain.web.mappers.member;

import com.mingyu.playground.domain.web.v1.member.dto.response.FindMemberResponseDto;
import com.mingyu.playground.domain.web.v1.member.entities.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    FindMemberResponseDto toFindMemberResponseDto(Member member);
}
