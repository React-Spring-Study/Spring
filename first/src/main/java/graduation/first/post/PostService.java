package graduation.first.post;

import graduation.first.category.Category;
import graduation.first.category.CategoryRepository;
import graduation.first.user.User;
import graduation.first.user.UserInfo;
import graduation.first.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long savePost(PostSaveRequestDto saveDto) {
        Category category = categoryRepository.findById(saveDto.getCategoryId())
                .orElseThrow(() -> new PostException(PostErrorCode.CATEGORY_NOT_FOUND));
        User writer = userRepository.findById(saveDto.getWriterId())
                .orElseThrow(() -> new PostException(PostErrorCode.WRITER_NOT_FOUND));

        Post post = Post.builder()
                .title(saveDto.getTitle())
                .content(saveDto.getContent())
                .category(category)
                .writer(writer)
                .build();

        Post saveOne = postRepository.save(post);

        return saveOne.getId();
    }

    @Transactional
    public PostResponseDto readOnePost(Long postId) {
        Post findOne = postRepository.findById(postId).orElseThrow(() -> new RuntimeException());
        User writer = findOne.getWriter();
        return new PostResponseDto(findOne.getId(),
                findOne.getTitle(),
                findOne.getContent(),
                new UserInfo(writer.getId(), writer.getName(), writer.getEmail()),
                findOne.getCategory().getId());
    }
}
