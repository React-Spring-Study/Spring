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
        return "게시물이 성공적으로 저장되었습니다.";
    }

    @GetMapping("/post/{postId}")
    public PostResponseDto readOnePost(@PathVariable Long postId) {
        return postService.readOnePost(postId);
    }

    @PutMapping("/post/{postId}")
    public String updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequestDto updateDto) {
        postService.updatePost(postId, updateDto);
        return "게시물이 성공적으로 수정되었습니다.";
    }
}
