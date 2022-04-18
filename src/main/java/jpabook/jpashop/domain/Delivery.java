package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)        // Ordinal 이면 숫자로 들어간다. -> Ordinal로 하면, 중간에 새로운 값이 들어오면 순서가 밀려서 무조건 String으로 한다.
    private DeliveryStatus status;      // READY, COMP
}
