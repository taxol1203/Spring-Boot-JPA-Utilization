package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)    // 상속 관계를 위해 싱글 테이블 전략을 사용한다. -> 한 테이블에 모든 필드를 넣는다.
@DiscriminatorColumn(name = "dtype")                     // 피 상속자들을 구분할 때 사용
@Getter @Setter
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    // 공통 속성들
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    // 비즈니스 로직을 엔티티에 두어, 응집도를 높인다. <- 데이터를 가지고 있는 쪽에 비즈니스 로직이 있는 것이 좋다
    /**
     * 재고 증가
    * */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소
     * */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
