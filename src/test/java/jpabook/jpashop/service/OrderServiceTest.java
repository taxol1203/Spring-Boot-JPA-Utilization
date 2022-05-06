package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired   EntityManager em;
    @Autowired   OrderService orderService;
    @Autowired   OrderRepository orderRepository;

    @Test
    public void order_test() throws Exception {
        //given
        Member member = createMember();
        Item book = createBook("시골 jpa", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        // "상품 주문시 상태는 ORDER"
        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        // 주문한 상품 종류 수가 정확해야한다.
        assertEquals(1, getOrder.getOrderItems().size());
        // 주문한 가격은 (가격 * 수량)이다.
        assertEquals(10000 * orderCount, getOrder.getTotalPrice());
        // 주문 수량만큼 재고가 줄어야한다.
        assertEquals(8, book.getStockQuantity());
    }

    /**
     * 상품 주문 재고 수량 초과 테스트
     * @throws Exception
     */
    @Test
    public void exceed_quantity_order_amount_test() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("비싼 책", 10000, 10);

        int orderAmount = 11;
        //when
        try{
            orderService.order(member.getId(), item.getId(), orderAmount);
        } catch (NotEnoughStockException e){
            return;
        }


        //then
        fail("재고 수량 부족 예외가 발생하여아 한다.");
    }

    /**
     * 주문 취소
     * @throws Exception
     */
    @Test
    public void cancel_order_test() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("jpa book", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());     // 주문 취소시 상태는 CANCEL이다.
        assertEquals(item.getStockQuantity(), 10);              // 주문이 취소된 상품은 그만큼 재고가 증가한다.

    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("busan", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}