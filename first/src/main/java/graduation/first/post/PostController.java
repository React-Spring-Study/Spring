package graduation.first.post;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post/new")
    public Long createPost(@RequestBody PostSaveRequestDto saveDto) {
        return postService.savePost(saveDto);
    }

    @GetMapping("/post/{postId}")
    public PostResponseDto readOnePost(@PathVariable Long postId) {
        return postService.readOnePost(postId);
    }
}
