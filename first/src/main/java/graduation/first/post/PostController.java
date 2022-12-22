package graduation.first.post;

import graduation.first.oauth.entity.UserAdapter;
import graduation.first.post.dto.PostResponseDto;
import graduation.first.post.dto.PostResponseVO;
import graduation.first.post.dto.PostSaveRequestDto;
import graduation.first.post.dto.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public String createPost(@AuthenticationPrincipal UserAdapter user, @RequestBody PostSaveRequestDto saveDto) {
        postService.savePost(user, saveDto);
        return "게시물 저장 성공";
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
    public String updatePost(@AuthenticationPrincipal UserAdapter user,
                             @PathVariable Long postId,
                             @RequestBody PostUpdateRequestDto updateDto) {
        postService.updatePost(user, postId, updateDto);
        return "게시물 수정 성공";
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@AuthenticationPrincipal UserAdapter user, @PathVariable Long postId) {
        postService.deletePost(user, postId);
        return "게시물 삭제 성공";
    }
}
