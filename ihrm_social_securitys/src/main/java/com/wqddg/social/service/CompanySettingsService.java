package com.wqddg.social.service;


import com.wqddg.domain.social_security.CompanySettings;
import com.wqddg.social.dao.CompanySettingsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanySettingsService {

    @Autowired
    private CompanySettingsDao settingsDao;

    /**
     * 根据企业id查询
     * @param companyId
     * @return
     */
    public CompanySettings findByid(String companyId) {
        CompanySettings companySettings=null;
        Optional<CompanySettings> byId = settingsDao.findById(companyId);
        if (byId!=null){
            companySettings=byId.get();
        }
        return companySettings;
    }

    /**
     * 保存企业
     * @param companySettings
     */
    public void save(CompanySettings companySettings) {
        companySettings.setIsSettings(1);
        settingsDao.save(companySettings);
    }
}
