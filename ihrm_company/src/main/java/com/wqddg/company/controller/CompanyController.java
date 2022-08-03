package com.wqddg.company.controller;

import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.company.serive.CompanyService;
import com.wqddg.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: wqddg
 * @ClassName CompanyController
 * @DateTime: 2022/1/3 23:09
 * @remarks : #
 */
@RestController
@RequestMapping("company")
@CrossOrigin
public class CompanyController {


    @Autowired
    private CompanyService companyService;

    @PostMapping
    public Result add(@RequestBody Company company){
        companyService.add(company);
        return new Result(ResultCode.SUCCESS);

    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id") String id,@RequestBody Company company){
        company.setId(id);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }
    @DeleteMapping("/{id}")
    public Result delect(@PathVariable("id") String id){
        companyService.delectByid(id);
        return new Result(ResultCode.SUCCESS);
    }
    @GetMapping("/{id}")
    public Result findByid(@PathVariable("id") String id){
        Company byid = companyService.findByid(id);
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(byid);
        return result;
    }
    @GetMapping
    public Result findAll() {
        List<Company> companies = companyService.finaAll();
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(companies);
        return result;
    }

}
