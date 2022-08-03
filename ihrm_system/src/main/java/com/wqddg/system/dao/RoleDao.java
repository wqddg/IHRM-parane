package com.wqddg.system.dao;

import com.wqddg.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author: wqddg
 * @ClassName RoleDao
 * @DateTime: 2022/1/6 11:53
 * @remarks : #
 */
public interface RoleDao extends JpaRepository<Role,String>, JpaSpecificationExecutor<Role> {
}
