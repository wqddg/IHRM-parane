package com.wqddg.common.exception;

import com.wqddg.common.entity.ResultCode;
import lombok.Getter;

/**
 * @Author: wqddg
 * @ClassName CommonExcption
 * @DateTime: 2022/1/3 23:47
 * @remarks : #自定义异常
 */
@Getter
public class CommonExcption extends Exception{

    private ResultCode resultCode;


    public CommonExcption(ResultCode resultCode){
        this.resultCode=resultCode;
    }
}
