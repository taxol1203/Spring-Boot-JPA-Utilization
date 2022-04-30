package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional              // test case에 @Transactional 어노테이션이 있으면, test 후 table을 rollback한다.
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    @Rollback(false)        // false로 두고 rollback을 막는다.
    public void signUp() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);
        //  em.flush();         // 테스트를위한 flush, jpa는 flush를 수행하면 insert가 된다.
        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test       // junit5 에서는 @Test(expected = ) 사용 불가
    public void find_Duplicate_User() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        //when
        memberService.join(member1);
        try{
            memberService.join(member2);        // 예외가 발생하여야한다.
        } catch (IllegalStateException e){
            return;
        }
        //then
        fail("예외가 발생하지 않았습니다.. 실패!");
    }
}