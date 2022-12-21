package graduation.first.post;

import graduation.first.category.Category;
import graduation.first.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    public List<Post> findAllByWriter(User writer);
    public List<Post> findAllByCategory(Category category);
}
