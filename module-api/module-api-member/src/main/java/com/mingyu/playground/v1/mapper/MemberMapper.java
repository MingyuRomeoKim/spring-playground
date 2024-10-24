package com.mingyu.playground.v1.mapper;


import com.mingyu.playground.dto.member.response.FindMemberResponseDto;

import com.mingyu.playground.v1.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    FindMemberResponseDto toFindMemberResponseDto(Member member);
}
