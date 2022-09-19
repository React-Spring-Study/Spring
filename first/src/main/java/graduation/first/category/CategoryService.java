package graduation.first.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(CategoryForm categoryForm) {
        Category category = Category.builder()
                .name(categoryForm.getName())
                .definition(categoryForm.getDefinition())
                .build();
        return categoryRepository.save(category);
    }

    public List<Category> listCategoriesForAdmin() {
        return categoryRepository.findAll();
    }

}
