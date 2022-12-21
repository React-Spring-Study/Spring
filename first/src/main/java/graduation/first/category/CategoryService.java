package graduation.first.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category saveCategory(@NotNull CategoryForm categoryForm) {
        Category category = Category.builder()
                .name(categoryForm.getName())
                .definition(categoryForm.getDefinition())
                .build();
        return categoryRepository.save(category);
    }

    @Transactional
    public List<Category> listCategoriesForAdmin() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category findOne(Long categoryId) {
        Category one = categoryRepository.findById(categoryId).
                orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        return one;
    }

    @Transactional
    public CategoryForm updateCategoryForm(Long categoryId) {

        Category category = findOne(categoryId);

        CategoryForm form = new CategoryForm();
        form.setName(category.getName());
        form.setDefinition(category.getDefinition());
        return form;
    }

    @Transactional
    public void removeCategory(Long categoryId) {
        Category findOne = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(findOne);
    }
}
