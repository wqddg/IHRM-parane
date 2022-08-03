package com.wqddg.system.controller;

import com.wqddg.common.entity.PageResult;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.common.exception.CommonExcption;
import com.wqddg.domain.system.Permission;
import com.wqddg.domain.system.User;
import com.wqddg.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName PermissionController
 * @DateTime: 2022/1/6 13:23
 * @remarks : #
 */
@CrossOrigin
@RestController
@RequestMapping("sys")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;


    @PostMapping("permission")
    public Result save(@RequestBody Map<String,Object> map) throws Exception {
        permissionService.add(map);
        return new Result(ResultCode.SUCCESS);
    }


    @PutMapping("permission/{id}")
    public Result update(@PathVariable("id") String id,@RequestBody Map<String,Object> map) throws Exception {
        map.put("id",id);
        permissionService.update(map);
        return new Result(ResultCode.SUCCESS);
    }


    @GetMapping("permission")
    public Result findAll(@RequestParam Map<String,Object> map){
        List<Permission> permissions = permissionService.finaAll(map);
        return new Result(ResultCode.SUCCESS,permissions);
    }

    @GetMapping(value = "permission/{id}",name = "我们的值")
    public Result findbyId(@PathVariable("id") String id){
        Map byid = permissionService.findByid(id);
        return new Result(ResultCode.SUCCESS,byid);
    }


    @DeleteMapping("permission/{id}")
    public Result delete(@PathVariable("id") String id) throws CommonExcption {
        permissionService.delectByid(id);
        return new Result(ResultCode.SUCCESS);
    }
}
