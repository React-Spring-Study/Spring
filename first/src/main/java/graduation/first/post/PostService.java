package graduation.first.post;

import graduation.first.category.Category;
import graduation.first.category.CategoryErrorCode;
import graduation.first.category.CategoryException;
import graduation.first.category.CategoryRepository;
import graduation.first.oauth.entity.UserAdapter;
import graduation.first.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

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
        return PostResponseDto.toDto(findOne);
    }

    @Transactional
    public Long updatePost(UserAdapter userAdapter,
                           Long postId,
                           PostUpdateRequestDto updateDto) {
        checkWriterAuth(userAdapter, updateDto.getWriter());
        Post findOne = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        Category category = categoryRepository.findByName(updateDto.getCategoryName());
        findOne.update(updateDto.getTitle(), updateDto.getContent(), category);
        return postId;
    }

    @Transactional
    public void deletePost(UserAdapter userAdapter, Long postId) {
        Post findOne = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        checkWriterAuth(userAdapter, findOne.getWriter());
        postRepository.delete(findOne);
    }

    private void checkWriterAuth(UserAdapter userAdapter, User writer) {
        if(userAdapter.getUser()!= writer)
            throw new UserException(UserErrorCode.USER_NOT_PERMITTED);
    }
}
