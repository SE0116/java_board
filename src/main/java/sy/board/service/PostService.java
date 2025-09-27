package sy.board.service;

import sy.board.domain.Post;
import sy.board.mapper.PostMapper;
import sy.board.support.PageParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

    @Transactional
    public Long write(Post post) {
        postMapper.insert(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public Post get(Long id, boolean increaseView) {
        if (increaseView) postMapper.increaseViewCount(id);
        return postMapper.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Post> list(PageParam page) {
        return postMapper.findAll(page.offset(), page.size(), page.keywordOrNull());
    }

    @Transactional(readOnly = true)
    public long count(PageParam page) {
        return postMapper.count(page.keywordOrNull());
    }

    @Transactional
    public void edit(Post post) {
        if (postMapper.update(post) == 0) throw new IllegalArgumentException("post not found: " + post.getId());
    }

    @Transactional
    public void delete(Long id) {
        postMapper.delete(id);
    }
}
