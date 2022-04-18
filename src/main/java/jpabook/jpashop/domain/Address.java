package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable         // 내장 타입에 들어간다고 나타내는 것. @Embedded만 있어도 됨
@Getter @Setter
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
