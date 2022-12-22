package graduation.first.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/new")
    public String showCategoryForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "categories/categoryForm";
    }

    @PostMapping("/new")
    public String createCategory(@Valid CategoryForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "categories/categoryForm";
        }
        Category category = categoryService.saveCategory(form);
        return "redirect:/categories/categoryList";
    }

    @GetMapping("/admin")
    public String readCategoriesForAdmin(Model model) {
        List<Category> categories = categoryService.listCategoriesForAdmin();
        model.addAttribute("categories", categories);
        return "categories/categoryList";
    }

    @GetMapping("/{categoryId}/edit")
    public String updateCategoryForm(@PathVariable Long categoryId, Model model) {
        CategoryForm updateForm = categoryService.updateCategoryForm(categoryId);
        model.addAttribute("updateForm",updateForm);
        return "categories/updateCategoryForm";
    }

    @PostMapping("/{categoryId}/edit")
    public String updateCategoryInfo(@Validated CategoryForm form) {
        categoryService.saveCategory(form);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) {
        categoryService.removeCategory(categoryId);
        return "redirect:/categories";
    }
}
