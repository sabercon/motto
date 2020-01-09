package cn.sabercon.motto.log.dao;

import cn.sabercon.motto.common.dao.BaseJpaRepository;
import cn.sabercon.motto.log.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ywk
 * @date 2020-01-03
 */
public interface ImageRepository extends BaseJpaRepository<Image> {

    Page<Image> findByUserIdAndNameLike(Long userId, String name, Pageable pageable);

    Page<Image> findByUserIdAndNameLikeAndType(Long userId, String name, String type, Pageable pageable);
}
