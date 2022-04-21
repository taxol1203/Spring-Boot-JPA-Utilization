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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)       // cascade : Order만 Persist 설정하면, 아래 OrderItem들도 함께 Persist가 된다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)       // FetchType을 Lazy로 두어, 지연로딩으로 설정
    @JoinColumn(name = "delivery_id")       // 연관관계의 주인으로 삼는다.
    private Delivery delivery;

    private LocalDateTime orderDate;        // 주문 시간

    @Enumerated(EnumType.STRING)        // 주의! - Enum은 해당 어노테이션을 쓰자
    private OrderStatus status;    // 주문상태 - [ORDER, CANCEL]

    //==연관관계 메서드==//
    public void setMember(Member member) {      // Member를 세팅할 때, 양방향으로 Member 테이블에 있는 orderList에 order.this 를 추가한다.
        this.member = member;
        member.getOrders().add(this);           // 양방향으로 세팅해주는 것
    }
    public void addOrderItem(OrderItem orderItem) {     // 원자 단위로 묶을 수 있다.
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {        // 연관관계 메서드는 관계의 주인이 갖는게 좋다.
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
