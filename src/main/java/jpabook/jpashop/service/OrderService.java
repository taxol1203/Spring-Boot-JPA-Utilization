package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     *
     * @param memberId
     * @param itemId
     * @param count
     * @return orderId
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        // 입력으로 주어진 member와 item의 id로 객체를 찾는다.
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createdOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        // 중요!, 주문을 할때 왜 주문(order)만 save하고 나머지는 save하는 코드가 없는지?
        // 왜나햐면 order entity에서 orderItem과 delivery의 연관관계에 cascade = CascadeType.ALL 로 선언되어 있기 때문이다.
        // 따라서, 별다른 persist 없이 자동으로 저장해 줌
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 삭제
     *
     * 여기서 중요한 것!
     * cancel을 하면, OrderStatus가 Cancel로 변하고, item의 개수(stock)가 올라간다.
     * 일반 sql이었다면 비즈니스 로직에 해당 값을 update하는 로직이 필요하지만
     * JPA를 활용하면, entity의 데이터를 변경하면, jpa가 변화를 감지하여(dirty checking, 변경 내역 감지)를 하여
     * 변경된 내역들에 대하여 update 쿼리가 자동으로 실행한다.
     * @param orderId
     */
    @Transactional
    public void cancelOrder(Long orderId){
        // 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 삭제
        order.cancel();
    }

    /*
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }
    */
}
