package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     *  검색 기능을 구현하는 첫번째 방법
     *  가장 기본적인 코드는 다음과 같다.
     *  파라미터로 주어진 member 이름과 status를 통해 where 조건으로 조회를 하면 되는 것
     *  하지만, 둘 중 값이 안넘어 오면?(null)
     *   => 동적 쿼리가 되어, 해당 where 조건을 무시하고 전부 가져오게 된다. *로 조회하게 됨
     *   => 그에 반해, mybatis나 i batis는 동적쿼리를 생성하기가 더 편하다
     *   개선이 필요하다.
     *
     * @param orderSearch
     * @return
     */
    public List<Order> findAll(OrderSearch orderSearch) {

        return em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status" +
                        " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)        // 최대 1000건까지 조회한다.
                .getResultList();
    }

    /**
     * 두번째 방법.
     * JPQL을 사용하되, 파라미터 값이 들어오는가에 따라 QUERY를 string으로 생성하는 법
     * 해당 방법은 오류의 위험이 매우 크다. -> 실수로 잘못 만들 가능성이 높다.
     *
     * @param orderSearch
     * @return
     */
    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)     // 위에서 만든 string을 query로 생성한다.
                .setMaxResults(1000); //최대 1000건

        // 파라미터 값 바인딩
        if (orderSearch.getOrderStatus() != null) {     // STATUS 값이 있으면
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {     // MEMBER 값이 있으면
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    /**
     * 마지막 방법
     * JPA Criteria로 처리
     *
     * jpa 표준 스펙이지만 이 방법도 권장하지는 않는다.
     *
     * Criteria는 JPQL이 동적쿼리를 생산할 때 도움을 준다.
     * 실제로 확인하여도 단순히 문자열로 Query를 생성하는 것 보다는 컴팩트하다
     *
     * 하지만 이 방법도 치명적인 단점이 있다.
     * -> 유지보수성이 0
     * 어떤 쿼리가 생성 될 지 상상이 안된다.
     *
     * @param orderSearch
     * @return
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);      // 조회 타입 설정

        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인

        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }
}