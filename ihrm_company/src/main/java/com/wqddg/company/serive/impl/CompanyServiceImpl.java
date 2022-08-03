package com.wqddg.company.serive.impl;

import com.wqddg.common.utils.IdWorker;
import com.wqddg.company.dao.CompanyDao;
import com.wqddg.company.serive.CompanyService;
import com.wqddg.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: wqddg
 * @ClassName CompanyServiceImpl
 * @DateTime: 2022/1/3 22:58
 * @remarks : #
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 增加企业
     * @param company
     */
    @Override
    public void add(Company company) {
        //基本属性的设置
        String id = idWorker.nextId() + "";
        company.setId(id);
        //默认的状态
        company.setAuditState("1");
        company.setCreateTime(new Date());
        company.setState(1);//已激活
        companyDao.save(company);
    }

    /**
     * 更新企业
     * @param company
     */
    @Override
    public void update(Company company) {
        Company company_update = companyDao.findById(company.getId()).get();
        company_update.setName(company.getName());
        company_update.setCompanyPhone(company.getCompanyPhone());
        companyDao.save(company_update);
    }

    @Override
    public void delectByid(String id) {
        companyDao.deleteById(id);
    }

    @Override
    public Company findByid(String id) {
        return companyDao.findById(id).get();
    }

    @Override
    public List<Company> finaAll() {
        return companyDao.findAll();
    }


}
