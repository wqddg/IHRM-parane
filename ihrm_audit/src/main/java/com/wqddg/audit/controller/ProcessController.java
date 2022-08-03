package com.wqddg.audit.controller;

import com.wqddg.audit.service.ProcessService;
import com.wqddg.common.controller.BaseController;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: wqddg
 * @ClassName ProcessController
 * @DateTime: 2022/1/20 23:04
 * @remarks : #
 */
@RestController
@RequestMapping("user/process")
@CrossOrigin
public class ProcessController extends BaseController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private RuntimeService runtimeService;
    /**
     * 部署新的流程
     */
    @PostMapping("deploy")
    public Result deplayProcess(@RequestParam("file")MultipartFile file) throws Exception {
        processService.deployProcess(file,"wqddg");
        return new Result(ResultCode.SUCCESS);
    }


    /**
     * 查询所有的流程定义
     * @return
     * @throws Exception
     */
    @GetMapping("definition")
    public Result deplayProcess() throws Exception {
        List list =processService.getProcessDefinitionList(companyId);
        return new Result(ResultCode.SUCCESS,list);
    }

    /**
     * 点击按钮是否
     * @return
     * @throws Exception
     */
    @GetMapping("suspend/{processKey}")
    public Result suspendProcess(@PathVariable String processKey) throws Exception {
        processService.suspendProcess(processKey,companyId);
        return new Result(ResultCode.SUCCESS);
    }




}
