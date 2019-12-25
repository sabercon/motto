package cn.sabercon.motto.log.dao;

import cn.sabercon.motto.common.dao.BaseJpaRepository;
import cn.sabercon.motto.log.entity.UserDetail;

/**
 * @author ywk
 * @date 2019-10-15
 */
public interface UserDetailRepository extends BaseJpaRepository<UserDetail> {

    UserDetail findByUserId(Long userId);

}
