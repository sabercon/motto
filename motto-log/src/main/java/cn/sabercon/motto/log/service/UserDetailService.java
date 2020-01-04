package cn.sabercon.motto.log.service;

import cn.sabercon.motto.common.util.NameUtils;
import cn.sabercon.motto.log.component.OssHelper;
import cn.sabercon.motto.log.dao.UserDetailRepository;
import cn.sabercon.motto.log.dto.UserRes;
import cn.sabercon.motto.log.entity.UserDetail;
import cn.sabercon.motto.log.util.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ywk
 * @date 2019-10-15
 */
@Service
@Transactional
public class UserDetailService {

    @Autowired
    private UserDetailRepository repository;

    public UserRes get() {
        UserDetail userDetail = repository.findByUserId(LoginUtils.getId());
        UserRes dto = new UserRes();
        BeanUtils.copyProperties(userDetail, dto);
        return dto;
    }

    public void update(UserRes dto) {
        UserDetail userDetail = repository.findByUserId(LoginUtils.getId());
        BeanUtils.copyProperties(dto, userDetail);
    }
}
