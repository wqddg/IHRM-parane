package com.wqddg.domain.company.response;

import com.wqddg.domain.company.Company;
import com.wqddg.domain.company.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: wqddg
 * @ClassName DeptListResult
 * @DateTime: 2022/1/4 14:53
 * @remarks : #
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptListResult {
    private String companyId;
    private String companyName;
    private String companyManage;
    private List<Department> depts;

    public DeptListResult(Company company,List depts){
        this.companyId=company.getId();
        this.companyManage=company.getLegalRepresentative();
        this.companyName=company.getName();
        this.depts=depts;
    }
}
