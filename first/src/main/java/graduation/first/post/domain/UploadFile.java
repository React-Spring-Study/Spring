package graduation.first.post.domain;

import graduation.first.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String uploadFileName;
    @NotBlank
    private String storeFileUrl;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public UploadFile(String uploadFileName, String storeFileUrl, Post post) {
        this.uploadFileName = uploadFileName;
        this.storeFileUrl = storeFileUrl;
        this.post = post;
    }
}
