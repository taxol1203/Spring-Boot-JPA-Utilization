package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
*
 * 따라서, @Transactional 어노테이션을 클래스 단위에 먼저 건다.
 * class에 해당 어노테이션을 붙히면 public 메서드는 다 @Transactional이 적용된다.
 *
 * 여기서 중요한건, read only이다.
 * read only하면 jpa가 성능을 올리게 된다.
 * 따라서 해당 클래스에 조회가 많으면 read only를 default로 걸어주고,
 * 데이터를 변경하는 메서드에 따로 @Transcational을 걸어준다.
* */

@Service
@Transactional(readOnly = true)                 // JPA에서 모든 데이터 변경은 트랜잭션 안에서 이루어져야 한다
@RequiredArgsConstructor                        // 6. 해당 어노테이션을 사용하면 final이 붙어있는 필드에 대하여 생성자가 자동으로 만들어진다.
public class MemberService {
    /**
    @Autowired
    private MemberRepository memberRepository;  // 1. 이렇게 Autowired를 사용 가능하다.

    private final MemberRepository memberRepository;  // 2. 하지만 필드에 Autowired를 걸지말고

    @Autowired                                  // 3. Constructor에 걸어주는 방법이 좋다.     <- 해당 Autowired를 생략해도 된다. why? 스프링은 생성자가 하나이면 자동으로 Autowired 를 건다.
    public MemberService(MemberRepository memberRepository) {   // 4. 이 방법이 좋은 이유는, 이후에 테스트를 위해 목 오브젝트를 넣을 수 있기 때문이다.
        this.memberRepository = memberRepository;               // 5. setter를 통해 memberRepository를 넣을 수 있지만, 중간에 변경되는 위험이 있으므로 생성자에 넣는다.
    }
     6. 하지만 보다시피 전부 주석이다, 왜냐면 Lombok의 RequiredArgsConstructor으로 해결 가능하기 때문이다.
        해당 어노테이션을 사용하면 final이 붙어있는 필드에 대하여 생성자가 자동으로 만들어진다.
     */

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional                              // 데이터를 변경하는 메서드이므로 따로 걸어준다
    public Long join(Member member){
        validateDuplicateMember(member);        // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());   // 이렇게 검증하려면, db의 Member table의 name 필드에 unique 조건을 거는게 낫다
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}

