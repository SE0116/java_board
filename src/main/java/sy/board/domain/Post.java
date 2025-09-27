package sy.board.domain;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Post {
    private Long id;

    @NotBlank @Size(max = 200)
    private String title;

    @NotBlank
    private String content;

    @NotBlank @Size(max = 50)
    private String author;

    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
