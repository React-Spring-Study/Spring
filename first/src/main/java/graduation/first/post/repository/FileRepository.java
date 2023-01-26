package graduation.first.post.repository;

import graduation.first.post.domain.Post;
import graduation.first.post.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<UploadFile, Long> {
    List<UploadFile> findAllByPost(Post post);
}
