package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable         // 내장 타입에 들어간다고 나타내는 것. @Embedded 만 있어도 됨
@Getter             // JPA의 임베디드 타입은 setter를 제거하고, constructor 만 있도록 한다.
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {       // jpa에서는 접근자를 protected 까지 허용한다.
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
