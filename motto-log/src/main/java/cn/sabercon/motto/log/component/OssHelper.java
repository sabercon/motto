package cn.sabercon.motto.log.component;

import cn.sabercon.motto.common.enums.ErrorCode;
import cn.sabercon.motto.common.exception.CommonException;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 阿里OSS对象存储工具类
 *
 * @author ywk
 * @date 2019-12-30
 */
@Slf4j
@Component
public class OssHelper {
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    @Value("${aliyun.oss.accessDomain}")
    private String accessDomain;

    /**
     * 上传文件
     *
     * @param file     要上传的文件
     * @param filename 文件名称，可用斜杆表示目录结构
     * @param metadata 要设置的 http 头信息
     * @return 访问文件的url
     */
    public String upload(MultipartFile file, String filename, ObjectMetadata metadata) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            PutObjectResult result = ossClient.putObject(bucketName, filename, file.getInputStream(), metadata);
        } catch (IOException | ClientException | OSSException e) {
            log.error("error happened when uploading file, filename:{}", filename);
            throw new CommonException(ErrorCode.UPLOAD_FAIL);
        }
        ossClient.shutdown();
        return accessDomain + filename;
    }

    /**
     * 上传可下载的文件
     */
    public String uploadFile(MultipartFile file, String filename) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentDisposition("attachment");
        return upload(file, filename, metadata);
    }

    /**
     * 上传可浏览的图像等文件
     */
    public String uploadImg(MultipartFile file, String filename) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentDisposition("inline");
        return upload(file, filename, metadata);
    }

    /**
     * 得到图片缩略图，高度等比例，默认宽度150
     *
     * @param url   原图片的 url
     * @param width 缩略图宽度
     * @return 缩略图的url
     */
    public static String getThumbnail(String url, Integer width) {
        if (width == null || width < 1) {
            width = 150;
        }
        String suffix = "?x-oss-process=image/resize,w_" + width;
        return url + suffix;
    }
}
