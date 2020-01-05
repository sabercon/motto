package cn.sabercon.motto.log.service;

import cn.sabercon.motto.common.dto.PageRes;
import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.common.util.EntityUtils;
import cn.sabercon.motto.common.util.NameUtils;
import cn.sabercon.motto.log.component.OssHelper;
import cn.sabercon.motto.log.dao.FileRepository;
import cn.sabercon.motto.log.entity.File;
import cn.sabercon.motto.log.util.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ywk
 * @date 2020-01-02
 */
@Service
@Transactional
public class FileService {

    @Autowired
    private FileRepository repository;

    public void save(File fileInfo) {
        File file = new File();
        EntityUtils.copyIgnoreNotCover(fileInfo, file);
        file.setDel(0);
        file.setUserId(LoginUtils.getId());
        repository.save(file);
    }

    public void delete(Long id) {
        repository.findById(id).ifPresent(e -> e.setDel(1));
    }

    public PageRes<File> list(PageReq pageReq) {
        pageReq.amendAll();
        Page<File> page = repository.findByUserIdAndNameLike(LoginUtils.getId(), pageReq.getFuzzyValue(), pageReq.toPageable());
        return PageRes.of(page);
    }

}
