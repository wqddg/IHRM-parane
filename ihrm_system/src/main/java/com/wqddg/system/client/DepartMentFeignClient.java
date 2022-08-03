package com.wqddg.system.client;

import com.wqddg.common.entity.Result;
import com.wqddg.domain.company.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.GET;

/**
 * @Author: wqddg
 * @ClassName DepartMentFeignClient
 * @DateTime: 2022/1/8 13:08
 * @remarks : #
 */
@FeignClient("ihrm-company")
public interface DepartMentFeignClient {

//    @GetMapping("company/department/{id}")
//    public Result findByid(@PathVariable("id") String id);



    @PostMapping("company/department/search")
    Department findByCode(@RequestParam("code") String code, @RequestParam("companyId") String companyId);
}
