package sy.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sy.board.domain.Post;
import sy.board.mapper.PostMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public java.util.List<Post> list(int page, int size, String q) {
        int offset = (Math.max(1, page) - 1) * Math.max(1, size);
        return postMapper.findAll(offset, size, q);
    }

    @Transactional(readOnly = true)
    public long count(String q) {
        return postMapper.count(q);
    }

    @Transactional
    public void deleteByIds(java.util.List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        postMapper.deleteByIds(ids);
    }

    @Transactional(readOnly = true)
    public Post get(Long id) {
        return postMapper.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + id));
    }

    public Long write(Post post) {
        postMapper.insert(post);
        return post.getId();
    }

    public void edit(Post post) {
        if (post.getId() == null) throw new IllegalArgumentException("id가 필요합니다.");
        postMapper.update(post);
    }

    public void delete(Long id) {
        postMapper.delete(id);
    }

    public Post getForView(Long id) {
        postMapper.increaseViewCount(id);
        return get(id);
    }
}
