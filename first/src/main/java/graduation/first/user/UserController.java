package graduation.first.user;

import graduation.first.common.response.ApiResponse;
import graduation.first.post.Post;
import graduation.first.post.PostListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse getUser() {
        User user = findLoggingInUser();
        UserInfo responseDto = UserInfo.toResponseDto(user);
        return ApiResponse.success("userInfo", responseDto);
    }

    @GetMapping("/me/posts")
    public PostListVO getMyposts() {
        User user = findLoggingInUser();
        List<Post> myPostLists = userService.getMyPostLists(user);
        return PostListVO.toResponseDto(myPostLists);
    }

    private User findLoggingInUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(principal.getUsername());
        return user;
    }
}
