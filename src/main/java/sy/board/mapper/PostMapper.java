package sy.board.mapper;

import sy.board.domain.Post;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface PostMapper {
    int insert(Post post);
    Optional<Post> findById(@Param("id") Long id);
    List<Post> findAll(@Param("offset") int offset, @Param("size") int size, @Param("keyword") String keyword);
    long count(@Param("keyword") String keyword);
    int update(Post post);
    int delete(@Param("id") Long id);
    int increaseViewCount(@Param("id") Long id);
}