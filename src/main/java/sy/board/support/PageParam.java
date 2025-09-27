package sy.board.support;

public record PageParam(int page, int size, String q) {
    public PageParam {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
    }
    public int offset() { return (page - 1) * size; }
    public String keywordOrNull() {
        return (q == null || q.isBlank()) ? null : "%" + q.trim() + "%";
    }
}
