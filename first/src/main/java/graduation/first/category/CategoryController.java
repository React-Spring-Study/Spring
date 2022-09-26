package graduation.first.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category/new")
    public String showCategoryForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "categories/categoryForm";
    }

    @PostMapping("/categories/new")
    public String createCategory(@Valid CategoryForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "categories/categoryForm";
        }
        Category category = categoryService.saveCategory(form);
        return "redirect:/categories/categoryList";
    }

    @GetMapping("/categories/admin")
    public String readCategoriesForAdmin(Model model) {
        List<Category> categories = categoryService.listCategoriesForAdmin();
        model.addAttribute("categories", categories);
        return "categories/categoryList";
    }

    @GetMapping("/categories/{categoryId}/edit")
    public String updateCategoryForm(@PathVariable Long categoryId, Model model) {
        CategoryForm updateForm = categoryService.updateCategoryForm(categoryId);
        model.addAttribute("updateForm",updateForm);
        return "categories/updateCategoryForm";
    }

    @PostMapping("/categories/{categoryId}/edit")
    public String updateCategoryInfo(@Validated CategoryForm form) {
        categoryService.saveCategory(form);
        return "redirect:/categories";
    }
}
