package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ㅁ")
@Getter @Setter
public class Album extends Item {
    private String author;
    private String isbn;
}
