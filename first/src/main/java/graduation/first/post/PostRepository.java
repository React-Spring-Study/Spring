package graduation.first.post;

import graduation.first.category.Category;
import graduation.first.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    public Page<Post> findAll(Pageable pageable);
    public Page<Post> findAllByWriter(User writer, Pageable pageable);
    public Page<Post> findAllByCategory(Category category, Pageable pageable);
}
