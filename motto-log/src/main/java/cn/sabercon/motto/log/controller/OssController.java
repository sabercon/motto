package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.dto.Result;
import cn.sabercon.motto.common.enums.ErrorCode;
import cn.sabercon.motto.common.util.AssertUtils;
import cn.sabercon.motto.common.util.NameUtils;
import cn.sabercon.motto.log.component.OssHelper;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ywk
 * @date 2020-01-04
 */
@CrossOrigin
@RestController
@Api("与OSS交互的接口")
@RequestMapping("oss")
public class OssController {

    @Autowired
    private OssHelper ossHelper;
    @Value("${aliyun.oss.dir.file}")
    private String filePath;
    @Value("${aliyun.oss.dir.img}")
    private String imgPath;

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

    @ApiOperation("上传文件")
    @PostMapping("file")
    public Result<String> uploadFile(MultipartFile file) {
        String filename = NameUtils.getOssName(file.getOriginalFilename());
        String url = ossHelper.upload(file, filePath + filename);
        return Result.success(url);
    }

    @ApiOperation("上传图片")
    @PostMapping("img")
    public Result<String> uploadImg(MultipartFile img) {
        AssertUtils.isTrue(PIC_TYPE_LIST.contains(img.getContentType()), ErrorCode.IMG_TYPE_WRONG);
        String imgName = NameUtils.getOssName(img.getOriginalFilename());
        String url = ossHelper.upload(img, imgPath + imgName);
        return Result.success(url);
    }

}
