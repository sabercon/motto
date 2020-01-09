package cn.sabercon.motto.log.service;

import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.common.dto.PageRes;
import cn.sabercon.motto.common.util.EntityUtils;
import cn.sabercon.motto.log.component.OssHelper;
import cn.sabercon.motto.log.dao.ImageRepository;
import cn.sabercon.motto.log.entity.Image;
import cn.sabercon.motto.log.util.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author ywk
 * @date 2020-01-03
 */
@Service
@Transactional
public class ImageService {

    @Autowired
    private ImageRepository repository;

    public void save(Image imageInfo) {
        Image image = new Image();
        EntityUtils.copyIgnoreNotCover(imageInfo, image);
        image.setThumbnailUrl(OssHelper.getThumbnail(image.getUrl(), 150));
        image.setDel(0);
        image.setUserId(LoginUtils.getId());
        repository.save(image);
    }

    public void delete(Long id) {
        repository.findById(id).filter(e -> e.getUserId().equals(LoginUtils.getId())).ifPresent(e -> e.setDel(1));
    }

    public PageRes<Image> list(PageReq pageReq) {
        pageReq.amendAll();
        Page<Image> page;
        if (StringUtils.hasLength(pageReq.getEqualValue())) {
            // query by type
            page = repository.findByUserIdAndNameLikeAndType(LoginUtils.getId(),
                    pageReq.getFuzzyValue(), pageReq.getEqualValue(), pageReq.toPageable());
        } else {
            page = repository.findByUserIdAndNameLike(LoginUtils.getId(), pageReq.getFuzzyValue(), pageReq.toPageable());
        }
        return PageRes.of(page);
    }

    public List<Image> listByLimit(Integer start, Integer size) {
        Long userId = LoginUtils.getId();
        return repository.listByLimit(userId, start, size);
    }
}
