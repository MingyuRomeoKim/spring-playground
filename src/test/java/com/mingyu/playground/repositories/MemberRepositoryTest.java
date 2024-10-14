package com.mingyu.playground.repositories;

import com.mingyu.playground.entities.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

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
        assertEquals(findMember.get().getEmail(), "mingyu");
    }

    @Test
    @DisplayName("이메일로 회원 조회 실패")
    void testFindByEmail_Fail() {

        // Act
        Optional<Member> findMember = memberRepository.findByEmail("mingyu2");

        // Assert
        assertTrue(findMember.isEmpty());
    }

}