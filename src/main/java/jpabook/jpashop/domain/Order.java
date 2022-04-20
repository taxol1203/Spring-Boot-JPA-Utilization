package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 여러개의 주문이 하나의 회원에게 이루어진다.
    @JoinColumn(name = "member_id")     // join할 key를 적는다 FK를 적는 듯 // FK를 가지고 있는 주체가, 주인이 된다. // 해당 값을 변경하면, FK가 변경된다.
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)       // cascade :
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)       //
    @JoinColumn(name = "delivery_id")       // 연관관계의 주인으로 삼는다.
    private Delivery delivery;

    private LocalDateTime orderDate;        // 주문 시간

    @Enumerated(EnumType.STRING)        // 주의! - Enum은 해당 어노테이션을 쓰자
    private OrderStatus status;    // 주문상태 - [ORDER, CANCEL]
}
