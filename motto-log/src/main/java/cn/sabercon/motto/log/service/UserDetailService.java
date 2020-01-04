package cn.sabercon.motto.log.service;

import cn.sabercon.motto.log.dao.UserDetailRepository;
import cn.sabercon.motto.log.dto.UserDto;
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

    public UserDto get() {
        UserDetail userDetail = repository.findByUserId(LoginUtils.getId());
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(userDetail, dto);
        return dto;
    }

    public void update(UserDto dto) {
        UserDetail userDetail = repository.findByUserId(LoginUtils.getId());
        BeanUtils.copyProperties(dto, userDetail);
    }
}
