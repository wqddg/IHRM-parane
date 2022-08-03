package com.wqddg.system.controller;

import com.wqddg.common.controller.BaseController;
import com.wqddg.common.entity.PageResult;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.domain.system. Role;
import com.wqddg.domain.system.response.RoleResult;
import com.wqddg.system.service. RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName  RoleController
 * @DateTime: 2022/1/5 22:26
 * @remarks : #
 */
@CrossOrigin
@RestController
@RequestMapping("sys")
public class RoleController extends BaseController {
    @Autowired
    private  RoleService  roleService;

    @PostMapping("role/assignPrem")
    public Result assignRoles(@RequestBody Map<String,Object> map){
        String user_id = (String) map.get("id");
        List<String> roleIds= (List<String>) map.get("permIds");
        roleService.assignRoles(user_id,roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping("role")
    public Result save(@RequestBody  Role  role){
        //设置保存的企业id
         role.setCompanyId(companyId);
         roleService.add( role);
        return new Result(ResultCode.SUCCESS);
    }



    @GetMapping("role")
    public Result findAll(int page, int pagesize, @RequestParam Map<String,Object> map){
        //获取当前的企业id
        map.put("companyId",companyId);
        Page< Role>  roles = (Page<Role>) roleService.findByPage(companyId,page, pagesize);
        PageResult result=new PageResult< Role>( roles.getTotalElements(), roles.getContent());
        return new Result(ResultCode.SUCCESS,result);
    }

    @GetMapping("role/{id}")
    public Result findbyId(@PathVariable("id") String id){
        Role byid =  roleService.findByid(id);
        RoleResult result=new RoleResult(byid);
        return new Result(ResultCode.SUCCESS,result);
    }


    @PutMapping("role/{id}")
    public Result update(@PathVariable("id") String id,@RequestBody  Role  role){
         role.setId(id);
         roleService.update( role);
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping("role/{id}")
    public Result delete(@PathVariable("id") String id){
         roleService.delectByid(id);
        return new Result(ResultCode.SUCCESS);
    }
    @GetMapping(value="/role/list")
    public Result findAll() throws Exception {
        List<Role> roleList = roleService.finaAll(companyId);
        return new Result(ResultCode.SUCCESS,roleList);
    }

}
