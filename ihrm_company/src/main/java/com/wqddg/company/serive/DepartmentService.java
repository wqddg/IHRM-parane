package com.wqddg.company.serive;

import com.wqddg.domain.company.Department;

import java.util.List;

/**
 * @Author: wqddg
 * @ClassName DepartmentService
 * @DateTime: 2022/1/4 14:22
 * @remarks : #
 */
public interface DepartmentService {



    void addDepartment(Department department);
    void updateDepartment(Department department);
    Department findByid(String id);
    List<Department> findAll(String companyId);

    void delectByid(String id);

    Department findbyCode(String code, String companyId);
}
