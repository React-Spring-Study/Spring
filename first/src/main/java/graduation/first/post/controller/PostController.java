package graduation.first.post.controller;

import graduation.first.common.response.StringResponse;
import graduation.first.oauth.entity.UserPrincipal;
import graduation.first.post.dto.PostResponseDto;
import graduation.first.post.dto.PostResponseVO;
import graduation.first.post.dto.PostSaveRequestDto;
import graduation.first.post.dto.PostUpdateRequestDto;
import graduation.first.post.service.PostService;
import graduation.first.user.UserService;
import graduation.first.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping
    public StringResponse createPost(@Valid @RequestPart PostSaveRequestDto saveDto,
                                     @RequestPart List<MultipartFile> files) throws IOException {
        User user = findLoggingInUser();
        postService.savePost(user, saveDto, files);
        return new StringResponse("게시물 저장 성공");
    }

    @GetMapping
    public Page<PostResponseVO> getPostList(Pageable pageable) {
        return postService.readPosts(pageable);
    }

    @GetMapping("category/{categoryId}")
    public Page<PostResponseVO> getPostListByCategory(@PathVariable Long categoryId, Pageable pageable) {
        return postService.readPostsByCategory(categoryId, pageable);
    }

    @GetMapping("/{postId}")
    public PostResponseDto readOnePost(@PathVariable Long postId) {
        return postService.readOnePost(postId);
    }

    @PutMapping("/{postId}")
    public StringResponse updatePost(@PathVariable Long postId,
                             @Valid @RequestBody PostUpdateRequestDto updateDto) {
        User user = findLoggingInUser();
        postService.updatePost(user, postId, updateDto);
        return new StringResponse("게시물 수정 성공");
    }

    @DeleteMapping("/{postId}")
    public StringResponse deletePost(@PathVariable Long postId) {
        User user = findLoggingInUser();
        postService.deletePost(user, postId);
        return new StringResponse("게시물 삭제 성공");
    }

    private User findLoggingInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(authentication.getName());
        return user;
    }
}
