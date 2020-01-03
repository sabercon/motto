package cn.sabercon.motto.log.service;

import cn.sabercon.motto.common.dto.PageRes;
import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.common.enums.ErrorCode;
import cn.sabercon.motto.common.util.AssertUtils;
import cn.sabercon.motto.common.util.NameUtils;
import cn.sabercon.motto.log.component.OssHelper;
import cn.sabercon.motto.log.dao.PictureRepository;
import cn.sabercon.motto.log.dto.PictureDto;
import cn.sabercon.motto.log.entity.Picture;
import cn.sabercon.motto.log.util.LoginUtils;
import com.google.common.collect.ImmutableList;
import org.apache.http.entity.ContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ywk
 * @date 2020-01-03
 */
@Service
@Transactional
public class PictureService {

    @Autowired
    private PictureRepository repository;
    @Autowired
    private OssHelper ossHelper;
    @Value("${aliyun.oss.dir.pic}")
    private String picPath;

    /**
     * 允许上传的图片格式
     */
    private static ImmutableList<String> PIC_TYPE_LIST;

    static {
        // 初始化构建列表
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        PIC_TYPE_LIST = builder
                .add(ContentType.IMAGE_BMP.getMimeType())
                .add(ContentType.IMAGE_GIF.getMimeType())
                .add(ContentType.IMAGE_JPEG.getMimeType())
                .add(ContentType.IMAGE_PNG.getMimeType())
                .add(ContentType.IMAGE_SVG.getMimeType())
                .add(ContentType.IMAGE_TIFF.getMimeType())
                .add(ContentType.IMAGE_WEBP.getMimeType())
                .build();
    }

    public void save(MultipartFile pic, String name) {
        AssertUtils.isTrue(PIC_TYPE_LIST.contains(pic.getContentType()), ErrorCode.PIC_TYPE_WRONG);
        String filename = NameUtils.getFilename(pic.getOriginalFilename());
        String url = ossHelper.upload(pic, picPath, filename);
        Picture picture = new Picture();
        picture.setName(name);
        picture.setUrl(url);
        picture.setThumbnailUrl(ossHelper.getThumbnail(url, 150));
        picture.setType(pic.getContentType());
        picture.setSize(pic.getSize());
        picture.setDel(0);
        picture.setUserId(LoginUtils.getId());
        repository.save(picture);
    }

    public void delete(Long id) {
        Picture picture = repository.getOne(id);
        picture.setDel(1);
    }

    public PageRes<PictureDto> list(PageReq pageReq) {
        pageReq.amendAll();
        Page<Picture> picPage = repository.findByUserIdAndNameLike(LoginUtils.getId(), pageReq.getFuzzyValue(), pageReq.getPageable());
        return PageRes.of(picPage.map(this::toDto));
    }

    private PictureDto toDto(Picture picture) {
        PictureDto pictureDto = new PictureDto();
        BeanUtils.copyProperties(picture, pictureDto);
        return pictureDto;
    }
}
