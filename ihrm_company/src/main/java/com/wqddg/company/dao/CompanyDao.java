package com.wqddg.company.dao;

import com.wqddg.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Author: wqddg
 * @ClassName CompanyDao
 * @DateTime: 2022/1/3 22:50
 * @remarks : #
 */
@Repository
public interface CompanyDao extends JpaRepository<Company,String>, JpaSpecificationExecutor<Company> {
}
