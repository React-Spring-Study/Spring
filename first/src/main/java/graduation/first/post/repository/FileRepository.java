package graduation.first.post.repository;

import graduation.first.post.domain.Post;
import graduation.first.post.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<UploadFile, Long> {

    @Query("select u from UploadFile u where u.post.id = :postId")
    List<UploadFile> findAllByPostId(Long postId);
}
