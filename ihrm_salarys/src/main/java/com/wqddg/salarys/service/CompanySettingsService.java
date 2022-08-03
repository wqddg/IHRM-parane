package com.wqddg.salarys.service;

import com.wqddg.domain.salarys.CompanySettingsSA;
import com.wqddg.salarys.dao.CompanySettingsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanySettingsService {
	
    @Autowired
    private CompanySettingsDao companySettingsDao;

    //根据id获取查询
    public CompanySettingsSA findById(String companyId) {
        Optional<CompanySettingsSA> optionalCompanySettins = companySettingsDao.findById(companyId);
        return optionalCompanySettins.isPresent() ? optionalCompanySettins.get() : null;
    }

    //保存配置
    public void save(CompanySettingsSA companySettings) {
        companySettings.setIsSettings(1);
        companySettingsDao.save(companySettings);
    }
}
