package graduation.first.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Category {

    @Id @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "category_definition")
    private String definition;

    @Builder
    public Category(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }
}
