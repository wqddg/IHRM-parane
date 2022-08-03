package com.wqddg.attendance.dao;

import com.wqddg.domain.atte.entity.DayOffConfig;
import com.wqddg.domain.social_security.CompanySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @Author: wqddg
 * @ClassName CompanySettingsDao
 * @DateTime: 2022/1/17 16:01
 * @remarks : #
 */
public interface CompanySettingsDao  extends CrudRepository<CompanySettings,String>, JpaRepository<CompanySettings, String>, JpaSpecificationExecutor<CompanySettings> {
}
