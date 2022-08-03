package com.wqddg.company.serive;

import com.wqddg.domain.company.Company;

import java.util.List;

/**
 * @Author: wqddg
 * @ClassName CompanyService
 * @DateTime: 2022/1/3 22:57
 * @remarks : #
 */
public interface CompanyService {



    void add(Company company);


    void update(Company company);

    void delectByid(String id);

    Company findByid(String id);


    public List<Company> finaAll();
}
