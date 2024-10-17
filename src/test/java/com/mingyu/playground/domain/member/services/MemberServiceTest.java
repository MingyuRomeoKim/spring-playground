package com.mingyu.playground.domain.member.services;

import com.mingyu.playground.common.error.PlayGroundCommonException;
import com.mingyu.playground.common.error.PlayGroundErrorCode;
import com.mingyu.playground.domain.member.dto.request.SaveMemberRequestDto;
import com.mingyu.playground.domain.member.dto.request.UpdateMemberRequestDto;
import com.mingyu.playground.domain.member.dto.response.FindMemberResponseDto;
import com.mingyu.playground.domain.member.entities.Member;
import com.mingyu.playground.domain.member.repositories.MemberRepository;
import com.mingyu.playground.domain.member.services.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new Member("민규", "mingyu", "1234", "01012345678", "서울시 강남구");
    }

    @Test
    @DisplayName("멤버 전체 조회")
    void testGetMembers() {
        // Arrange
        when(memberRepository.findAll()).thenReturn(List.of(member));
        // Act
        List<FindMemberResponseDto> members = memberService.getMembers();
        // Assert
        Assertions.assertThat(members).isNotEmpty();
        Assertions.assertThat(members.get(0).getName()).isEqualTo("민규");
    }

    @Test
    @DisplayName("이메일로 멤버 조회 성공")
    void testGetMemberByEmail_Success() {
        // Arrange
        when(memberRepository.findByEmail("mingyu")).thenReturn(java.util.Optional.of(member));
        // Act
        FindMemberResponseDto findMember = memberService.getMemberByEmail("mingyu");
        // Assert
        Assertions.assertThat(findMember).isNotNull();
        Assertions.assertThat(findMember.getName()).isEqualTo("민규");
    }

    @Test
    @DisplayName("이메일로 멤버 조회 못 찾음")
    void testGetMemberByEmail_NotFound() {
        // Arrange
        when(memberRepository.findByEmail("mingyu")).thenReturn(java.util.Optional.empty());
        // Act & Assert
        assertThatThrownBy(() -> memberService.getMemberByEmail("mingyu")).isInstanceOf(PlayGroundCommonException.class).hasMessage(PlayGroundErrorCode.COMMON_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("멤버 저장")
    void testSaveMember() {
        // Arrange
        SaveMemberRequestDto saveMemberRequestDto = SaveMemberRequestDto.builder()
                .name("민규")
                .email("mingyu")
                .password("1234")
                .phone("01012345678")
                .address("서울시 강남구")
                .build();

        Member saveMember = saveMemberRequestDto.toEntity();
        when(memberRepository.save(saveMember)).thenReturn(member);

        // Act
        memberService.saveMember(saveMemberRequestDto);

        // Assert
        verify(memberRepository, times(1)).save(any(Member.class));
    }


    @Test
    @DisplayName("멤버 삭제 성공")
    void testDeleteMember_Success() {
        // Arrange
        when(memberRepository.findByEmail("mingyu")).thenReturn(java.util.Optional.of(member));
        // Act
        memberService.deleteMemberByEmail("mingyu");
        // Assert
        verify(memberRepository, times(1)).delete(any(Member.class));
    }

    @Test
    @DisplayName("멤버 삭제 못 찾음")
    void testDeleteMember_NotFound() {
        // Arrange
        when(memberRepository.findByEmail("mingyu")).thenReturn(java.util.Optional.empty());
        // Act & Assert
        assertThatThrownBy(() -> memberService.deleteMemberByEmail("mingyu")).isInstanceOf(PlayGroundCommonException.class).hasMessage(PlayGroundErrorCode.COMMON_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("멤버 수정 성공")
    void testUpdateMember_Success() {
        // Arrange
        when(memberRepository.findByEmail("mingyu")).thenReturn(java.util.Optional.of(member));

        // Act
        UpdateMemberRequestDto updateMemberRequestDto = UpdateMemberRequestDto.builder()
                .name("민규")
                .password("1234")
                .phone("01012345678")
                .address("서울시 강남구")
                .build();
        memberService.updateMemberByEmail("mingyu", updateMemberRequestDto);

        // Assert
        Assertions.assertThat(member.getName()).isEqualTo("민규");
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("멤버 수정 못 찾음")
    void testUpdateMember_NotFound() {
        // Arrange
        UpdateMemberRequestDto updateMemberRequestDto = UpdateMemberRequestDto.builder()
                .name("민규")
                .phone("010111111111")
                .address("인천시 부평구")
                .build();

        when(memberRepository.findByEmail("mingyu123123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> memberService.updateMemberByEmail("mingyu123123",UpdateMemberRequestDto.builder().build())).isInstanceOf(PlayGroundCommonException.class).hasMessage(PlayGroundErrorCode.COMMON_NOT_FOUND.getMessage());
    }

}