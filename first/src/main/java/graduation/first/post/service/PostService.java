package graduation.first.post.service;

import graduation.first.category.Category;
import graduation.first.category.CategoryErrorCode;
import graduation.first.category.CategoryException;
import graduation.first.category.CategoryRepository;
import graduation.first.post.domain.Post;
import graduation.first.post.domain.UploadFile;
import graduation.first.post.dto.*;
import graduation.first.post.repository.FileRepository;
import graduation.first.post.repository.PostRepository;
import graduation.first.post.exception.PostErrorCode;
import graduation.first.post.exception.PostException;
import graduation.first.user.domain.User;
import graduation.first.user.exception.UserErrorCode;
import graduation.first.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;

    @Transactional
    public Long savePost(User writer, PostSaveRequestDto saveDto, List<MultipartFile> files) throws IOException {
        log.info("New Post Writer = {}",writer.toString());
        Category category = categoryRepository.findByName(saveDto.getCategoryName())
                .orElseThrow(() -> new PostException(PostErrorCode.CATEGORY_NOT_FOUND));
        Post post = Post.builder()
                .title(saveDto.getTitle())
                .content(saveDto.getContent())
                .category(category)
                .writer(writer)
                .build();
        Long id = postRepository.save(post).getId();
        if (!files.isEmpty())
            s3Service.uploadFile(post, files);
        return id;
    }

    @Transactional
    public Page<PostResponseVO> readPosts(Pageable pageable) {
        return PostResponseVO.toVoList(postRepository.findAll(pageable));
    }

    @Transactional
    public Page<PostResponseVO> readPostsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        return PostResponseVO.toVoList(postRepository.findAllByCategory(category, pageable));
    }

    @Transactional
    public PostResponseDto readOnePost(Long postId) {
        PostResponseDto responseDto = PostResponseDto.toDto(getPostById(postId));
        List<UploadFile> entityList = fileRepository.findAllByPostId(postId);
        if (!entityList.isEmpty())
            responseDto.setFiles(UploadFileResponse.toResponseList(entityList));
        else
            responseDto.setFiles(new ArrayList<>());
        return responseDto;
    }

    @Transactional
    public Long updatePost(User writer,
                           Long postId,
                           PostUpdateRequestDto updateDto) {
        Post post = getPostById(postId);
        checkWriterAuth(writer, post.getWriter().getUserId());
        Category category = categoryRepository.findByName(updateDto.getCategoryName())
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        post.update(updateDto.getTitle(), updateDto.getContent(), category);
        return postId;
    }

    @Transactional
    public void deletePost(User writer, Long postId) {
        Post findOne = getPostById(postId);
        checkWriterAuth(writer, findOne.getWriter().getUserId());
        postRepository.delete(findOne);
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }

    private void checkWriterAuth(User writer, String writerId) {
        log.info("writer.getUserId()={}", writer.getUserId());
        log.info("writerId={}", writerId);
        if (writer.getUserId().equals(writerId))
            throw new UserException(UserErrorCode.USER_NOT_PERMITTED);
    }
}
