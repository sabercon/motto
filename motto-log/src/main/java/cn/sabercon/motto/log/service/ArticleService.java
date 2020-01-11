package cn.sabercon.motto.log.service;

import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.common.dto.PageRes;
import cn.sabercon.motto.common.util.EntityUtils;
import cn.sabercon.motto.log.dao.ArticleRepository;
import cn.sabercon.motto.log.entity.Article;
import cn.sabercon.motto.log.util.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author ywk
 * @date 2020-01-03
 */
@Service
@Transactional
public class ArticleService {

    @Autowired
    private ArticleRepository repository;

    public void save(Article articleInfo) {
        if (articleInfo.getId() == null) {
            // create
            Article article = new Article();
            EntityUtils.copyIgnoreNotCover(articleInfo, article);
            article.setUserId(LoginUtils.getId());
            article.setDel(0);
            repository.save(article);
        } else {
            // update
            repository.findById(articleInfo.getId()).filter(e -> e.getUserId().equals(LoginUtils.getId()))
                    .ifPresent(article -> EntityUtils.copyIgnoreNotCover(articleInfo, article));
        }
    }

    public void delete(Long id) {
        repository.findById(id).filter(e -> e.getUserId().equals(LoginUtils.getId())).ifPresent(e -> e.setDel(1));
    }

    public Article get(Long id) {
        return repository.findById(id).filter(e -> e.getUserId().equals(LoginUtils.getId())).orElse(null);
    }

    @Transactional(readOnly = true)
    public PageRes<Article> list(PageReq pageReq) {
        pageReq.amendAll();
        Page<Article> page;
        if (StringUtils.hasLength(pageReq.getEqualValue())) {
            // query by type
            page = repository.findByUserIdAndTitleLikeAndType(LoginUtils.getId(),
                    pageReq.getFuzzyValue(), pageReq.getEqualValue(), pageReq.toPageable());
        } else {
            page = repository.findByUserIdAndTitleLike(LoginUtils.getId(), pageReq.getFuzzyValue(), pageReq.toPageable());
        }
        // no need to return text when getting page
        page.forEach(article -> {
            article.setText(null);
            article.setRawJson(null);
        });
        return PageRes.of(page);
    }

}
