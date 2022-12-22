package graduation.first.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode {
    CATEGORY_NOT_FOUND("게시판을 찾을 수 없습니다.");

    private String defaultErrorMessage;
}
