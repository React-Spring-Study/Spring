package graduation.first.post;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public String createPost(@RequestBody PostSaveRequestDto saveDto) {
        postService.savePost(saveDto);
        return "게시물 저장 성공";
    }

    @GetMapping("/{categoryId}")
    public PostListVO getPostListByCategory(@PathVariable Long categoryId) {
        return postService.readPostsByCategory(categoryId);
    }

    @GetMapping("/{postId}")
    public PostResponseDto readOnePost(@PathVariable Long postId) {
        return postService.readOnePost(postId);
    }

    @PutMapping("/{postId}")
    public String updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequestDto updateDto) {
        postService.updatePost(postId, updateDto);
        return "게시물 수정 성공";
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "게시물 삭제 성공";
    }
}
