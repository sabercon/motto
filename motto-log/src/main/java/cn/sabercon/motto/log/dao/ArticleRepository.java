package cn.sabercon.motto.log.dao;

import cn.sabercon.motto.common.dao.BaseJpaRepository;
import cn.sabercon.motto.log.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ywk
 * @date 2020-01-03
 */
public interface ArticleRepository extends BaseJpaRepository<Article> {

    Page<Article> findByUserIdAndTitleLike(Long userId, String title, Pageable pageable);

    Page<Article> findByUserIdAndTitleLikeAndType(Long userId, String title, String type, Pageable pageable);
}
