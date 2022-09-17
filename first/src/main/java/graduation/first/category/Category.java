package graduation.first.category;

import javax.persistence.*;

@Entity
public class Category {

    @Id @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "category_definition")
    private String definition;
}
