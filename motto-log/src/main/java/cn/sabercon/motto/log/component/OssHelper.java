package cn.sabercon.motto.log.component;

import cn.sabercon.motto.common.enums.ErrorCode;
import cn.sabercon.motto.common.exception.CommonException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
     * @param dir   OSS的储存目录
     * @param filename 文件名称，需包含后缀名
     * @return 访问文件的url
     */
    public String upload(MultipartFile file, String dir, String filename) {
        // 得到文件路径
        if (! dir.endsWith("/")) {
            dir += "/";
        }
        String filePath = dir + filename;
        log.info("the upload file path:{}", filePath);

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(bucketName, filePath, file.getInputStream());
        } catch (IOException e) {
            log.error("error happened when getting the inputStream of file, filePath:{}", filePath);
            throw new CommonException(ErrorCode.FAIL);
        }
        ossClient.shutdown();
        return String.format("https://%s.%s/%s", bucketName, endpoint, filePath);
    }
}
