package graduation.first.post;

import graduation.first.category.Category;
import graduation.first.category.CategoryRepository;
import graduation.first.user.User;
import graduation.first.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Transactional
    public Long savePost(PostSaveRequestDto saveDto) {
        Category category = categoryRepository.findById(saveDto.getCategory_id()).orElseThrow(() -> new RuntimeException());
        User writer = userRepository.findById(saveDto.getWriter_id()).orElseThrow(() -> new RuntimeException());

        Post post = Post.builder()
                .title(saveDto.getTitle())
                .content(saveDto.getContent())
                .category(category)
                .writer(writer)
                .build();

        return postRepository.save(post).getId();
    }
}
