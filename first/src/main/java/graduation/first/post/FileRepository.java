package graduation.first.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<UploadFile, Long> {
    List<UploadFile> findAllByPost(Post post);
}
