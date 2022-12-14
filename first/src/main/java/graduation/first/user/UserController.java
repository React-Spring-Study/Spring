package graduation.first.user;

import graduation.first.common.response.ApiResponse;
import graduation.first.post.Post;
import graduation.first.post.dto.PostResponseVO;
import graduation.first.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
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
    public Page<PostResponseVO> getMyPosts(Pageable pageable) {
        User user = findLoggingInUser();
        Page<Post> myPostLists = userService.getMyPostLists(user, pageable);
        return PostResponseVO.toVoList(myPostLists);
    }

    private User findLoggingInUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(principal.getUsername());
        return user;
    }
}
