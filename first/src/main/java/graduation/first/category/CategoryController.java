package graduation.first.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category/new")
    public String createCategoryForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "categories/categoryForm";
    }

    @PostMapping("/category/new")
    public String createCategory(@Valid CategoryForm form, BindingResult bindingResult) {
        Category category = categoryService.createCategory(form);
        return "redirect:/categories/categoryList";
    }

    @GetMapping("/categories")
    public String readCategoriesForAdmin(Model model) {
        List<Category> categories = categoryService.listCategoriesForAdmin();
        model.addAttribute("categories", categories);
        return "categories/categoryList";
    }
}
