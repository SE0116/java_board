package sy.board.support;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageParam {
    private int page;
    private int size;
    private String q; // 검색어

    public PageParam(int page, int size, String q) {
        this.page = Math.max(page, 1);
        this.size = Math.max(size, 1);
        this.q = q;
    }

    public int offset() {
        return (page - 1) * size;
    }

    public String keywordLikeOrNull() {
        if (q == null || q.isBlank()) {
            return null;
        }
        return "%" + q.trim() + "%";
    }
}
