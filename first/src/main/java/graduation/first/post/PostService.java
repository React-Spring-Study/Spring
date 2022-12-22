package graduation.first.post;

import graduation.first.category.Category;
import graduation.first.category.CategoryErrorCode;
import graduation.first.category.CategoryException;
import graduation.first.category.CategoryRepository;
import graduation.first.oauth.entity.UserAdapter;
import graduation.first.post.dto.PostResponseDto;
import graduation.first.post.dto.PostResponseVO;
import graduation.first.post.dto.PostSaveRequestDto;
import graduation.first.post.dto.PostUpdateRequestDto;
import graduation.first.post.exception.PostErrorCode;
import graduation.first.post.exception.PostException;
import graduation.first.user.domain.User;
import graduation.first.user.exception.UserErrorCode;
import graduation.first.user.exception.UserException;
import graduation.first.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public Long savePost(UserAdapter userAdapter, PostSaveRequestDto saveDto) {
        User writer = userAdapter.getUser();
        Category category = categoryRepository.findByName(saveDto.getCategoryName())
                .orElseThrow(() -> new PostException(PostErrorCode.CATEGORY_NOT_FOUND));
        Post post = Post.builder()
                .title(saveDto.getTitle())
                .content(saveDto.getContent())
                .category(category)
                .writer(writer)
                .build();
        return postRepository.save(post).getId();
    }

    @Transactional
    public Page<PostResponseVO> readPosts(Pageable pageable) {
        return PostResponseVO.toVoList(postRepository.findAll(pageable));
    }

    @Transactional
    public Page<PostResponseVO> readPostsByCategory(String categoryName, Pageable pageable) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        return PostResponseVO.toVoList(postRepository.findAllByCategory(category, pageable));
    }

    @Transactional
    public PostResponseDto readOnePost(Long postId) {
        return PostResponseDto.toDto(getPostById(postId));
    }

    @Transactional
    public Long updatePost(UserAdapter userAdapter,
                           Long postId,
                           PostUpdateRequestDto updateDto) {
        Post post = getPostById(postId);
        checkWriterAuth(userAdapter, post.getWriter().getId());
        Category category = categoryRepository.findByName(updateDto.getCategoryName())
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        post.update(updateDto.getTitle(), updateDto.getContent(), category);
        return postId;
    }

    @Transactional
    public void deletePost(UserAdapter userAdapter, Long postId) {
        Post findOne = getPostById(postId);
        checkWriterAuth(userAdapter, findOne.getWriter().getId());
        postRepository.delete(findOne);
    }

    private Post getPostById(Long postId) {
        Post findOne = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        return findOne;
    }

    private void checkWriterAuth(UserAdapter userAdapter, Long writerId) {
        if(userAdapter.getUser().getId()!= writerId)
            throw new UserException(UserErrorCode.USER_NOT_PERMITTED);
    }
}
