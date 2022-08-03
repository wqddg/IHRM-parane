package com.wqddg.attendance.controller;

import com.wqddg.attendance.service.ConfigurationService;
import com.wqddg.common.controller.BaseController;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.domain.atte.entity.AttendanceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wqddg
 * @ClassName ConfigController
 * @DateTime: 2022/1/17 0:21
 * @remarks : #
 */
@RestController
@RequestMapping("cfg")
public class ConfigController extends BaseController {

    @Autowired
    private ConfigurationService configurationService;

    /**
     * 获取考勤信息
     * @param departmentId
     * @return
     */
    @PostMapping("atte/item")
    public Result atteConfig(String departmentId){
        AttendanceConfig atteConfig =configurationService.getAtteConfig(companyId,departmentId);
        return new Result(ResultCode.SUCCESS,atteConfig);
    }

    @PutMapping("atte")
    public Result atteConfig(@RequestBody AttendanceConfig attendanceConfig){
        attendanceConfig.setCompanyId(companyId);

        configurationService.saveAtteConfig(attendanceConfig);
        return new Result(ResultCode.SUCCESS);

    }


}
