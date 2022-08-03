package com.wqddg.system.controller;

import com.baidu.aip.util.Base64Util;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.domain.system.response.FaceLoginResult;
import com.wqddg.domain.system.response.QRCode;
import com.wqddg.system.service.FaceLoginService;
import com.wqddg.system.utils.BaiduAiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sys/faceLogin")
public class FaceLoginController {
    @Autowired
    private FaceLoginService loginService;

    @Autowired
    private BaiduAiUtil aiUtil;
    /**
     * 获取刷脸登录二维码
     */
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public Result qrcode() throws Exception {
        QRCode qrCode = loginService.getQRCode();
        return new Result(ResultCode.SUCCESS,qrCode);
    }

    /**
     * 检查二维码：登录页面轮询调用此方法，根据唯一标识code判断用户登录情况
     */
    @RequestMapping(value = "/qrcode/{code}", method = RequestMethod.GET)
    public Result qrcodeCeck(@PathVariable(name = "code") String code) throws Exception {
        FaceLoginResult result = loginService.checkQRCode(code);
        return new Result(ResultCode.SUCCESS,result);
    }

    /**
     * 人脸登录：根据落地页随机拍摄的面部头像进行登录
     *          根据拍摄的图片调用百度云AI进行检索查找
     */
    @RequestMapping(value = "/{code}", method = RequestMethod.POST)
    public Result loginByFace(@PathVariable(name = "code") String code, @RequestParam(name = "file") MultipartFile attachment) throws Exception {
        String userId = loginService.loginByFace(code, attachment);
        if (userId!=null){
            return new Result(ResultCode.SUCCESS);
        }else {
            return new Result(ResultCode.FAIL);
        }
    }


    /**
     * 图像检测，判断图片中是否存在面部头像
     */
    @RequestMapping(value = "/checkFace", method = RequestMethod.POST)
    public Result checkFace(@RequestParam(name = "file") MultipartFile attachment) throws Exception {
        String encode = Base64Util.encode(attachment.getBytes());
        Boolean aBoolean = aiUtil.faceCheck(encode);
        if (aBoolean){
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

}
