package graduation.first.post;

import graduation.first.category.Category;
import graduation.first.category.CategoryErrorCode;
import graduation.first.category.CategoryException;
import graduation.first.category.CategoryRepository;
import graduation.first.oauth.entity.UserAdapter;
import graduation.first.oauth.entity.UserPrincipal;
import graduation.first.user.User;
import graduation.first.user.UserInfo;
import graduation.first.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long savePost(UserAdapter userAdapter, PostSaveRequestDto saveDto) {
        User writer = userAdapter.getUser();
        Category category = categoryRepository.findById(saveDto.getCategoryId())
                .orElseThrow(() -> new PostException(PostErrorCode.CATEGORY_NOT_FOUND));

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
    public Page<PostResponseVO> readPosts(Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        return PostResponseVO.toVoList(all);
    }

    @Transactional
    public Page<PostResponseVO> readPostsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        Page<Post> postList = postRepository.findAllByCategory(category, pageable);
        return PostResponseVO.toVoList(postList);
    }

    @Transactional
    public PostResponseDto readOnePost(Long postId) {
        Post findOne = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        User writer = findOne.getWriter();
        return new PostResponseDto(findOne.getId(),
                findOne.getTitle(),
                findOne.getContent(),
                new UserInfo(writer.getId(), writer.getName(), writer.getEmail(), writer.getProfileImg()),
                findOne.getCategory().getId());
    }

    @Transactional
    public Long updatePost(UserAdapter userAdapter,
                           Long postId,
                           PostUpdateRequestDto updateDto) {
        if(userAdapter.getUser()!=updateDto.getWriter())
            throw new RuntimeException();
        Post findOne = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        Category category = categoryRepository.findByName(updateDto.getCategoryName());
        findOne.update(updateDto.getTitle(), updateDto.getContent(), category);
        return postId;
    }

    @Transactional
    public void deletePost(Long postId) {
        Post findOne = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        postRepository.delete(findOne);
    }
}
