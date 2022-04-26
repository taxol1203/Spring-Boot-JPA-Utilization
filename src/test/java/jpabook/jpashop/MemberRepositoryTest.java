package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional          // test case에 @Transactional 어노테이션이 있으면, test 후 table을 rollback한다.
    @Rollback(false)        // false로 두고 rollback을 막는다.
    public void testMember() throws Exception {
//        //given
//        Member member = new Member();
//        member.setName("memberA");
//
//        //when
//        Long saveId = memberRepository.save(member);        // 생성한 member를 db에 넣는다.
//        Member findMember = memberRepository.find(saveId);  // 앞선 리턴 id를 조회하여 db에서 member를 얻는다.
//
//        //then
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());    // 처음에 생성한 member와 db의 member가 같은지 확인
//        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
//        Assertions.assertThat(findMember).isEqualTo(member);                    // 같을 까? 같다! -> 하나의 @Transaction (영속성)내부에서 식별자가 같으면 같은 entity로 생각하낟.
    }
}