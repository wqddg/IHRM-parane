package com.wqddg.company.controller;

import com.wqddg.common.controller.BaseController;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.company.serive.CompanyService;
import com.wqddg.company.serive.DepartmentService;
import com.wqddg.domain.company.Company;
import com.wqddg.domain.company.Department;
import com.wqddg.domain.company.response.DeptListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: wqddg
 * @ClassName DepartmentController
 * @DateTime: 2022/1/4 14:32
 * @remarks : #
 */
@CrossOrigin
@RestController
@RequestMapping("company")
public class DepartmentController extends BaseController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CompanyService companyService;




    @PostMapping("department")
    public Result save(@RequestBody Department department){
        //设置保存的企业id
        department.setCompanyId(companyId);
        departmentService.addDepartment(department);
        return new Result(ResultCode.SUCCESS);
    }



    @GetMapping("department")
    public Result findAll(){
        List<Department> all = departmentService.findAll(companyId);
        Result result = new Result(ResultCode.SUCCESS);
        Company byid = companyService.findByid(companyId);
        DeptListResult deptListResult=new DeptListResult(byid,all);
        result.setData(deptListResult);
        return result;
    }

    @GetMapping("department/{id}")
    public Result findbyId(@PathVariable("id") String id){
        Department byid = departmentService.findByid(id);
        return new Result(ResultCode.SUCCESS,byid);
    }


    @PutMapping("department/{id}")
    public Result update(@PathVariable("id") String id,@RequestBody Department department){
        department.setId(id);
        departmentService.updateDepartment(department);
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping("department/{id}")
    public Result delete(@PathVariable("id") String id){
        departmentService.delectByid(id);
        return new Result(ResultCode.SUCCESS);
    }



    @PostMapping("department/search")
    public Department findByCode(@RequestParam("code") String code,@RequestParam("companyId") String companyId){
        System.out.println("我们访问成功"+code+"   "+companyId);
        return departmentService.findbyCode(code,companyId);
    }


}
