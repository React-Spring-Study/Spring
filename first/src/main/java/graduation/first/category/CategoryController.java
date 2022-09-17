package graduation.first.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String readCategories(Model model) {
        List<Category> categories = categoryService.listCategories();
        model.addAttribute("categories", categories);
        return "categories/categoryList";
    }
}
