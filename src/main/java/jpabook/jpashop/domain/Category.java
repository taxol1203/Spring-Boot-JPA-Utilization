package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",                          // category와 item의 사이 테이블을 조인한다는 뜻
            joinColumns = @JoinColumn(name = "category_id"),    // category_item의 category_id를 매핑
            inverseJoinColumns = @JoinColumn(name = "item_id")) // category_item의 item_id을 매핑
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")        // 여러명의 자식이 하나의 부모를 바라보므로
    private Category parent;

    @OneToMany(mappedBy = "parent")         // child 테이블은 parent 테이블에 의해 mapped 되었다는 뜻
    private List<Category> child = new ArrayList<>();
}
