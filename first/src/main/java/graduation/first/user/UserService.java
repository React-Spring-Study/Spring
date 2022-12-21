package graduation.first.user;

import graduation.first.post.Post;
import graduation.first.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Post> getMyPostLists(User writer, Pageable pageable) {return postRepository.findAllByWriter(writer, pageable); }
}
