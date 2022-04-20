package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    @Embedded               // 내장 타입에 들어간다고 나타내는 것. @Embeddable 만 있어도 됨
    private Address address;

    // 하나의 회원이 여러개의 주문을 한다.
    // mappedBy가 선언되었으므로, 읽기 전용이 된다. 즉, 해당 orders 리스트가 변경되어도 Fk는 변경되지 않음
    @OneToMany(mappedBy = "member")                 // 관계의 주체가 아니므로, Order table의 member 필드에 의해 매핑 되었다고 표기
    private List<Order> orders = new ArrayList<>();


}
