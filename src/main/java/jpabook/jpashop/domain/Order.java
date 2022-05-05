package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 생성자를 protected로 만든다. -> createOrder와 같이 생성 메서드 외에는 생성자를 허용하지 않는다는 뜻
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

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
    */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);     // 해당 주문은 이제 취소 상태이다.
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();             // 취소하였으므로, 처음 주문하여서 빠졌던 item 수 만큼 다시 채워준다.
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
