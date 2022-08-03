package com.wqddg.salarys.service;

import com.alibaba.fastjson.JSON;
import com.wqddg.common.entity.PageResult;
import com.wqddg.common.entity.Result;
import com.wqddg.common.utils.*;
import com.wqddg.domain.atte.bo.AtteSalaryStatisticsBO;
import com.wqddg.domain.atte.vo.AtteSalaryStatisticsVO;
import com.wqddg.domain.employee.UserCompanyPersonal;
import com.wqddg.domain.salarys.SalaryArchiveDetail;
import com.wqddg.domain.salarys.Settings;
import com.wqddg.domain.salarys.UserSalary;
import com.wqddg.domain.salarys.UserSalaryChange;

import com.wqddg.salarys.constant.Constant;
import com.wqddg.salarys.constant.FormOfEmploymentEnum;
import com.wqddg.salarys.constant.TaxCountingEnum;
import com.wqddg.salarys.dao.UserSalaryChangeDao;
import com.wqddg.salarys.dao.UserSalaryDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

@Service
public class SalaryService {
	
    @Autowired
    private UserSalaryDao userSalaryDao;

    //定薪或者调薪
    public void saveUserSalary(UserSalary userSalary) {
        userSalaryDao.save(userSalary);
    }

	//查询用户薪资
    public UserSalary findUserSalary(String userId) {
        Optional<UserSalary> optional = userSalaryDao.findById(userId);
        return optional.isPresent() ? optional.get() : null;
    }

	//分页查询当月薪资列表
	public PageResult findAll(Integer page, Integer pageSize, String companyId) {
		Page page1 = userSalaryDao.findPage(companyId, PageRequest.of(page - 1, pageSize));
		return new PageResult(page1.getTotalElements(),page1.getContent());
	}

}
