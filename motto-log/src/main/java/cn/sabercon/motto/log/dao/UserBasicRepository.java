package cn.sabercon.motto.log.dao;

import cn.sabercon.motto.common.dao.BaseJpaRepository;
import cn.sabercon.motto.log.entity.UserBasic;

/**
 * @author ywk
 * @date 2019-10-15
 */
public interface UserBasicRepository extends BaseJpaRepository<UserBasic> {

    UserBasic findByUsername(String username);

    UserBasic findByPhone(String phone);

}
