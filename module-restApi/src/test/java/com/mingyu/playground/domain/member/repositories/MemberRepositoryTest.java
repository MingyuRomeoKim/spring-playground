package com.mingyu.playground.domain.member.repositories;

import com.mingyu.playground.domain.web.v1.member.entities.Member;
import com.mingyu.playground.domain.web.v1.member.repositories.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일로 회원 조회 성공")
    void testFindByEmail_Success() {

        // Arrange
        Member member = new Member("민규", "mingyu", "1234", "01012345678", "서울시 강남구");
        memberRepository.save(member);

        // Act
        Optional<Member> findMember = memberRepository.findByEmail("mingyu");

        // Assert
        assertTrue(findMember.isPresent());
        Assertions.assertThat(findMember.get().getName()).isEqualTo("민규");
    }

    @Test
    @DisplayName("이메일로 회원 조회 실패")
    void testFindByEmail_Fail() {
        // Act
        Optional<Member> findMember = memberRepository.findByEmail("mingyu2");

        // Assert
        Assertions.assertThat(findMember).isEmpty();
    }

}