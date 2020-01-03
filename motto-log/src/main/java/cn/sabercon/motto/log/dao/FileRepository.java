package cn.sabercon.motto.log.dao;

import cn.sabercon.motto.common.dao.BaseJpaRepository;
import cn.sabercon.motto.log.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ywk
 * @date 2020-01-02
 */
public interface FileRepository extends BaseJpaRepository<File> {

    Page<File> findByUserIdAndNameLike(Long userId, String name, Pageable pageable);
}
