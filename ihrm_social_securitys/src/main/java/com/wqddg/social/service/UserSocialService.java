package com.wqddg.social.service;

import com.wqddg.common.entity.PageResult;
import com.wqddg.domain.social_security.UserSocialSecurity;
import com.wqddg.social.dao.UserSocialSecurityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserSocialService {

    @Autowired
    private UserSocialSecurityDao userSocialSecurityDao;

    /**
     * 分页查询用户的社保数据
     * @param page
     * @param pageSize
     * @param companyId
     * @return
     */
    public PageResult findAll(Integer page, Integer pageSize, String companyId) {
        Page page1 = userSocialSecurityDao.findPage(companyId, PageRequest.of(page - 1, pageSize));


        return new PageResult(page1.getTotalElements(),page1.getContent());
    }

    public UserSocialSecurity finbyId(String id) {
        UserSocialSecurity socialSecurity = userSocialSecurityDao.findById(id).get();
        return socialSecurity;
    }

    public void save(UserSocialSecurity uss) {
        userSocialSecurityDao.save(uss);
    }

    //根据id查询
    public UserSocialSecurity findById(String id) {
        Optional<UserSocialSecurity> optional = userSocialSecurityDao.findById(id);
        return optional.isPresent()? optional.get() : null;
    }
}
