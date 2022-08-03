package com.wqddg.system.service;

import com.wqddg.domain.system.response.FaceLoginResult;
import com.wqddg.domain.system.response.QRCode;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: wqddg
 * @ClassName FaceLoginService
 * @DateTime: 2022/1/10 23:51
 * @remarks : #
 */
public interface FaceLoginService {
    //创建二维码
    public QRCode getQRCode() throws Exception ;
    //根据唯一标识，查询用户是否登录成功
    public FaceLoginResult checkQRCode(String code);

    //扫描二维码之后，使用拍摄照片进行登录
    public String loginByFace(String code, MultipartFile attachment) throws Exception ;
    //构造缓存key
//    private String getCacheKey(String code);
}
