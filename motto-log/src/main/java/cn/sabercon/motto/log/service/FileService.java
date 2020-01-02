package cn.sabercon.motto.log.service;

import cn.sabercon.motto.common.dto.CommonPage;
import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.common.util.NameUtils;
import cn.sabercon.motto.log.component.OssHelper;
import cn.sabercon.motto.log.dao.FileRepository;
import cn.sabercon.motto.log.dto.FileDto;
import cn.sabercon.motto.log.entity.File;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private OssHelper ossHelper;
    @Value("${aliyun.oss.dir.file}")
    private String filePath;

    public void save(MultipartFile file, String name) {
        String filename = NameUtils.getFilename(file.getOriginalFilename());
        String url = ossHelper.upload(file, filePath, filename);
        File fileEntity = new File();
        fileEntity.setName(name);
        fileEntity.setUrl(url);
        fileEntity.setType(file.getContentType());
        fileEntity.setSize(file.getSize());
        fileEntity.setDel(0);
        repository.save(fileEntity);
    }

    public void delete(Long id) {
        File file = repository.getOne(id);
        file.setDel(1);
    }

    public CommonPage<FileDto> list(PageReq pageReq) {
        pageReq.amendAll();
        Page<File> filePage = repository.findByDelAndNameLike(0, pageReq.getLike(), pageReq.getPageable());
        return CommonPage.of(filePage.map(this::toDto));
    }

    private FileDto toDto(File file) {
        FileDto fileDto = new FileDto();
        BeanUtils.copyProperties(file, fileDto);
        return fileDto;
    }
}
