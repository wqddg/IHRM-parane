package com.wqddg.system.service;

import com.wqddg.domain.system.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName UserService
 * @DateTime: 2022/1/3 22:57
 * @remarks : #
 */
public interface UserService {



    void add(User company);


    void update(User company);

    void delectByid(String id);

    User findByid(String id);


    public Page<User> finaAll(Map<String,Object> map, int page, int size) ;

    void assignRoles(String user_id, List<String> roleIds);

    User findMobile(String mobile);

    void insetALl(List<User> list, String companyId, String companyName);

    String upload(String id, MultipartFile file) throws IOException;
}
