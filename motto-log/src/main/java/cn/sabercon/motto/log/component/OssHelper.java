package cn.sabercon.motto.log.component;

import cn.sabercon.motto.common.enums.ErrorCode;
import cn.sabercon.motto.common.exception.CommonException;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.PutObjectResult;
import com.google.common.base.Charsets;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.digester.DocumentProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;

/**
 * 阿里OSS对象存储工具类
 *
 * @author ywk
 * @date 2019-12-30
 */
@Component
@Slf4j
public class OssHelper {
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 上传文件
     *
     * @param file     要上传的文件
     * @param dir      OSS的储存目录
     * @param filename 文件名称，需包含后缀名
     * @return 访问文件的url
     */
    public String upload(MultipartFile file, String dir, String filename) {
        // 得到文件路径
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        String filePath = dir + filename;
        log.info("the upload file path:{}", filePath);

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            PutObjectResult result = ossClient.putObject(bucketName, filePath, file.getInputStream());
        } catch (IOException | ClientException | OSSException e) {
            log.error("error happened when uploading file, filePath:{}", filePath);
            throw new CommonException(ErrorCode.UPLOAD_FAIL);
        }
        ossClient.shutdown();
        return String.format("https://%s.%s/%s", bucketName, endpoint, filePath);
    }

    /**
     * 得到图片缩略图，高度等比例，默认宽度150
     *
     * @param url 原图片的 url
     * @param width 缩略图宽度
     * @return 缩略图的url
     */
    public String getThumbnail(String url, Integer width) {
        if (width == null || width < 1) {
            width =150;
        }
        String suffix = "?x-oss-process=image/resize,w_" + width;
        return url + suffix;
    }
}
