package cn.sabercon.motto.log.dao;

import cn.sabercon.motto.common.dao.BaseJpaRepository;
import cn.sabercon.motto.log.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author ywk
 * @date 2020-01-03
 */
public interface ImageRepository extends BaseJpaRepository<Image> {

    Page<Image> findByUserIdAndNameLike(Long userId, String name, Pageable pageable);

    Page<Image> findByUserIdAndNameLikeAndType(Long userId, String name, String type, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM `image` WHERE `user_id` = ?1 AND `del` = 0 ORDER BY `id` DESC LIMIT ?2, ?3")
    List<Image> listByLimit(Long userId, Integer start, Integer size);
}
