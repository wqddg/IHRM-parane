package com.wqddg.common.handler;

import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.common.exception.CommonExcption;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wqddg
 * @ClassName BaseExceptionHandler
 * @DateTime: 2022/1/3 23:42
 * @remarks : # 自定义异常处理类
 */
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request, HttpServletResponse response, Exception e){
        if (e.getClass()== CommonExcption.class){
            CommonExcption e1 = (CommonExcption) e;
            return new Result(e1.getResultCode());
        }
        Result result=new Result(ResultCode.SERVER_ERROR);
        return result;
    }

}
