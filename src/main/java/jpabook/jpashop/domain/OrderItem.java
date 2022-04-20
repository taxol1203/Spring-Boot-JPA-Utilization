package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)   // 주문된 아이템(여러개)이 주문(한개)을 바라보는 방향
    @JoinColumn(name = "order_id")
    private Order order;

    private Long orderPrice;        // 주문 가격
    private Long count;             // 주문 수량량
}
