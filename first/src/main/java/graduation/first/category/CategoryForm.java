package graduation.first.category;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CategoryForm {

    private Long id;

    @NotEmpty
    private String name;
    private String definition;
}
