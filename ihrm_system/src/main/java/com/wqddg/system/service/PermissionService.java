package com.wqddg.system.service;

import com.wqddg.common.exception.CommonExcption;
import com.wqddg.domain.system.Permission;
import com.wqddg.domain.system.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName UserService
 * @DateTime: 2022/1/3 22:57
 * @remarks : #
 */
public interface PermissionService {

    void add(Map<String,Object> map) throws Exception;


    void update(Map<String,Object> map) throws Exception;

    void delectByid(String id) throws CommonExcption;

    Map findByid(String id);


     List<Permission> finaAll(Map<String,Object> map) ;
}
