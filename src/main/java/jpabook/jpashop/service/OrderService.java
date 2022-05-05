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


}
