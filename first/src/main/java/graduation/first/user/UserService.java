package graduation.first.user;

import graduation.first.post.Post;
import graduation.first.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    public List<Post> getMyPostLists(User writer) {return postRepository.findAllByWriter(writer); }
}
