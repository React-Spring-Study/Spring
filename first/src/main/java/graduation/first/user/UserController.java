package graduation.first.user;

import graduation.first.post.domain.Post;
import graduation.first.post.dto.PostResponseVO;
import graduation.first.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserInfo getUser() {
        User user = findLoggingInUser();
        UserInfo responseDto = UserInfo.toResponseDto(user);
        return responseDto;
    }

    @GetMapping("/me/posts")
    public Page<PostResponseVO> getMyPosts(Pageable pageable) {
        User user = findLoggingInUser();
        Page<Post> myPostLists = userService.getMyPostLists(user, pageable);
        return PostResponseVO.toVoList(myPostLists);
    }

    private User findLoggingInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(authentication.getName());
        return user;
    }
}
