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

    private int orderPrice;        // 주문 가격, 이미 item이 있는데 왜? => 할인 받을 수도 있으니
    private int count;             // 주문 수량량

    //==생성 메서드==//
    public static OrderItem createdOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);        // 주문한 양 만큼 재고를 까줘야한다.
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);      // 취소하였으므로, 처음 주문하여서 빠졌던 item 수 만큼 다시 채워준다.
    }

    //== 조회 로직 == //

    /**
     * 주문 상품 전체 가격 조회
     * @return 주문상품 전체 가격
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
