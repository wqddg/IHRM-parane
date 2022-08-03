package com.wqddg.system.service.impl;

import com.wqddg.common.service.BaseService;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.common.utils.PermissionConstants;
import com.wqddg.domain.system.Permission;
import com.wqddg.domain.system. Role;
import com.wqddg.system.dao.PermissionDao;
import com.wqddg.system.dao. RoleDao;
import com.wqddg.system.service. RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @Author: wqddg
 * @ClassName  RoleServiceImpl
 * @DateTime: 2022/1/3 22:58
 * @remarks : #
 */
@Service
public class RoleServiceImpl extends BaseService implements  RoleService {
    @Autowired
    private  RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 增加企业
     * @param role
     */
    @Override
    public void add( Role role) {
        //基本属性的设置
        String id = idWorker.nextId() + "";
        role.setId(id);
        roleDao.save(role);
    }

    /**
     * 更新企业
     * @param role
     */
    @Override
    public void update( Role role) {
         Role role_update = roleDao.findById(role.getId()).get();
         role_update.setDescription(role.getDescription());
         role_update.setName(role.getName());
        roleDao.save(role_update);
    }

    @Override
    public void delectByid(String id) {
        roleDao.deleteById(id);
    }

    @Override
    public  Role findByid(String id) {
        return roleDao.findById(id).get();
    }

    @Override
    public  List<Role> finaAll(String companyId) {
        return  roleDao.findAll(getSpec(companyId));
    }

    public Page<Role> findByPage(String companyId, int page, int size) {
        return roleDao.findAll(getSpec(companyId), PageRequest.of(page-1, size));
    }

    @Override
    public void assignRoles(String role_id, List<String> parmIds) {
        Role role = roleDao.findById(role_id).get();
        Set<Permission> permissions=new HashSet<>();
        for (String parmId : parmIds) {
            Permission permission = permissionDao.findById(parmId).get();
            List<Permission> byTypeAndPid = permissionDao.findByTypeAndPid(PermissionConstants.PY_API, permission.getId());
            permissions.addAll(byTypeAndPid);
            permissions.add(permission);
        }
        role.setPermissions(permissions);
        roleDao.save(role);
    }


}
