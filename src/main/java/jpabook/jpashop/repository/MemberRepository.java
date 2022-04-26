package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext             // JPA가 제공하는 기능             -> 이거 지우고, RequiredArgsConstructor 넣어도 댐( 대신 final 넣어야한다!), Autowired로는 안된다
    private EntityManager em;       // springboot가 EntityManager가 자동으로 DI 해준다. -> stater-data-jpa dependency가 주입해 줌

    public void save(Member member){
        em.persist(member);         // EM이 member를 알아서 저장
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);       // member를 저장하고 id 값을 반환한다. [ Commend와 query를 분할해라 ]
    }

    public List<Member> findAll(){  // 전체 조회
        return em.createQuery("select m from Member m", Member.class)   // JPQL 쿼리는 엔티티 객체를 기준으로 동작한다.
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)      // :name <- 파라미터를 바인딩
                .setParameter("name", name)     // <- 파라미터 연결
                .getResultList();
    }
}
