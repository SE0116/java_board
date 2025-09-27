package sy.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sy.board.domain.Post;
import sy.board.mapper.PostMapper;
import sy.board.support.PageParam;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public List<Post> list(PageParam page) {
        return postMapper.findAll(page.offset(), page.getSize(), page.keywordLikeOrNull());
    }

    @Transactional(readOnly = true)
    public long count(PageParam page) {
        return postMapper.count(page.keywordLikeOrNull());
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
