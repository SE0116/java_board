package sy.board.mapper;

import sy.board.domain.Post;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface PostMapper {
    List<Post> findAll(@Param("offset") int offset,
                       @Param("size") int size,
                       @Param("q") String q);
    long count(@Param("q") String q);
    int deleteByIds(@Param("ids") List<Long> ids);
    int insert(Post post);
    Optional<Post> findById(@Param("id") Long id);
    int update(Post post);
    int delete(@Param("id") Long id);
    int increaseViewCount(@Param("id") Long id);
}