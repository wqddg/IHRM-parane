package com.wqddg.system.dao;

import com.wqddg.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author: wqddg
 * @ClassName UserDao
 * @DateTime: 2022/1/5 21:52
 * @remarks : #
 */
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User>{
    User findByMobile(String mobile);
}
