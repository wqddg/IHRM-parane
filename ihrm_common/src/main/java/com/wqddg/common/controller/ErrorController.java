package com.wqddg.common.controller;

import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wqddg
 * @ClassName ErrorController
 * @DateTime: 2022/1/8 0:08
 * @remarks : #
 */
@RestController
@CrossOrigin
public class ErrorController {


    @RequestMapping("autherror")
    public Result autherror(int code){
        return code==1?new Result(ResultCode.UNAUTHENTICATED):new Result(ResultCode.UNAUTHORISE);
    }
}
