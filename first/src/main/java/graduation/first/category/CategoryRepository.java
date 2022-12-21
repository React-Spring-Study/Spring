package graduation.first.category;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Category findByName(String categoryName);
}
