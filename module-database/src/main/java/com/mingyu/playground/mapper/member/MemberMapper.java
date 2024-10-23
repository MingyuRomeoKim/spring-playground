package com.mingyu.playground.mapper.member;


import com.mingyu.playground.dto.member.response.FindMemberResponseDto;
import com.mingyu.playground.entity.member.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    FindMemberResponseDto toFindMemberResponseDto(Member member);
}
