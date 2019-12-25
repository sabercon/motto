package cn.sabercon.motto.log.service;

import cn.sabercon.motto.common.util.EntityUtils;
import cn.sabercon.motto.log.dao.UserDetailRepository;
import cn.sabercon.motto.log.dto.UserDetailDto;
import cn.sabercon.motto.log.entity.UserDetail;
import cn.sabercon.motto.log.util.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ywk
 * @date 2019-10-15
 */
@Service
@Transactional
public class UserDetailService {

    @Autowired
    private UserDetailRepository repository;

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

}
