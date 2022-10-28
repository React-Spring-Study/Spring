package graduation.first.post;

import graduation.first.category.Category;
import graduation.first.user.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Builder
    public Post(String title, String content, Category category, User writer) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.writer = writer;
    }

}
