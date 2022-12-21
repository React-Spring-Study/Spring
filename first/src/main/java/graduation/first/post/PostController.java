package graduation.first.post;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post/new")
    public String createPost(@RequestBody PostSaveRequestDto saveDto) {
        postService.savePost(saveDto);
        return "게시물 저장 성공";
    }

    @GetMapping("/post/{postId}")
    public PostResponseDto readOnePost(@PathVariable Long postId) {
        return postService.readOnePost(postId);
    }

    @PutMapping("/post/{postId}")
    public String updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequestDto updateDto) {
        postService.updatePost(postId, updateDto);
        return "게시물 수정 성공";
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "게시물 삭제 성공";
    }
}
