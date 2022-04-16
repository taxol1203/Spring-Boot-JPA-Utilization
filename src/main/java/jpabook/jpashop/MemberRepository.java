package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {     // entity를 찾아주는 역할.

    @PersistenceContext
    private EntityManager em;       // springboot가 EntityManager가 자동으로 DI 해준다. -> stater-data-jpa dependency가 주입해 줌

    public Long save(Member member){
        em.persist(member);
        return member.getId();      // member를 저장하고 id 값을 반환한다. [ Commend와 query를 분할해라 ]
    }

    public Member find(Long id){
        return em.find(Member.class, id);

    }
}
