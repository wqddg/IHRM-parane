package com.wqddg.company.dao;

import com.wqddg.domain.company.Company;
import com.wqddg.domain.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Author: wqddg
 * @ClassName DepartmentDao
 * @DateTime: 2022/1/4 14:21
 * @remarks : #
 */
@Repository
public interface DepartmentDao extends JpaRepository<Department,String>, JpaSpecificationExecutor<Department> {
    Department findByCodeAndCompanyId(String code,String companyId);
}
