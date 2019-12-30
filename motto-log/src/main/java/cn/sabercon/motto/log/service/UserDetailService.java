package cn.sabercon.motto.log.service;

import cn.sabercon.motto.common.util.EntityUtils;
import cn.sabercon.motto.common.util.NameUtils;
import cn.sabercon.motto.log.component.OssHelper;
import cn.sabercon.motto.log.dao.UserDetailRepository;
import cn.sabercon.motto.log.dto.UserDetailDto;
import cn.sabercon.motto.log.entity.UserDetail;
import cn.sabercon.motto.log.util.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * @author ywk
 * @date 2019-10-15
 */
@Service
@Transactional
public class UserDetailService {

    @Autowired
    private UserDetailRepository repository;
    @Autowired
    private OssHelper ossHelper;
    @Value("${aliyun.oss.dir.avatar}")
    private String avatarPath;

    public UserDetailDto get() {
        UserDetail userDetail = repository.findByUserId(LoginUtils.getId());
        UserDetailDto dto = new UserDetailDto();
        BeanUtils.copyProperties(userDetail, dto);
        return dto;
    }

    public void update(UserDetailDto dto) {
        UserDetail userDetail = repository.findByUserId(LoginUtils.getId());
        EntityUtils.copyPropertiesIgnoreEmpty(dto, userDetail);
    }

    /**
     * 上传头像
     *
     * @param avatar 头像文件
     * @return 头像访问路径
     */
    public String uploadAvatar(MultipartFile avatar) {
        String filename = NameUtils.getFilename(avatar.getOriginalFilename());
        return ossHelper.upload(avatar, avatarPath, filename);
    }
}
