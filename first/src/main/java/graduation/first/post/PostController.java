package graduation.first.post;

import graduation.first.common.response.StringResponse;
import graduation.first.post.dto.PostResponseDto;
import graduation.first.post.dto.PostResponseVO;
import graduation.first.post.dto.PostSaveRequestDto;
import graduation.first.post.dto.PostUpdateRequestDto;
import graduation.first.post.service.PostService;
import graduation.first.user.UserService;
import graduation.first.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping
    public StringResponse createPost(@Valid @RequestPart PostSaveRequestDto saveDto) throws IOException {
        User user = findLoggingInUser();
        postService.savePost(user, saveDto);
        return new StringResponse("게시물 저장 성공");
    }

    @GetMapping
    public Page<PostResponseVO> getPostList(Pageable pageable) {
        return postService.readPosts(pageable);
    }

    @GetMapping("/{categoryName}")
    public Page<PostResponseVO> getPostListByCategory(@PathVariable String categoryName, Pageable pageable) {
        return postService.readPostsByCategory(categoryName, pageable);
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
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(principal.getUsername());
        return user;
    }
}
