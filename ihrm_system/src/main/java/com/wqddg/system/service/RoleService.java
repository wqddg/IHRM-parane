package com.wqddg.system.service;

import com.wqddg.domain.system.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName RoleService
 * @DateTime: 2022/1/3 22:57
 * @remarks : #
 */
public interface RoleService {



    void add(Role role);


    void update(Role role);

    void delectByid(String id);

    Role findByid(String id);


    List<Role> finaAll(String companyId) ;


    Page<Role> findByPage(String companyId, int page, int size);

    void assignRoles(String user_id, List<String> roleIds);
}
