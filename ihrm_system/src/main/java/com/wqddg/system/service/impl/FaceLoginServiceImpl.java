package com.wqddg.system.service.impl;


import com.baidu.aip.util.Base64Util;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.domain.system.User;
import com.wqddg.domain.system.response.FaceLoginResult;
import com.wqddg.domain.system.response.QRCode;
import com.wqddg.system.dao.UserDao;
import com.wqddg.system.service.FaceLoginService;
import com.wqddg.system.utils.BaiduAiUtil;
import com.wqddg.system.utils.QRCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Service
public class FaceLoginServiceImpl implements FaceLoginService {

    @Value("${qr.url}")
    private String url;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private QRCodeUtil qrCodeUtil;
    @Autowired
    private BaiduAiUtil aiUtil;
    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;


	//创建二维码
    @Override
    public QRCode getQRCode() throws Exception {
        //1.创建唯一标识
        String code = idWorker.nextId() + "";
        //2.生成二维码(url地址)
        String content = url+"?code="+code;
        System.out.println(content);
        String file = qrCodeUtil.crateQRCode(content);
        System.out.println(file);
        //3.存入当前二维码状态（存入redis）
        FaceLoginResult result = new FaceLoginResult("-1");
        redisTemplate.boundValueOps(getCacheKey(code)).set(result,10, TimeUnit.MINUTES);//状态对象，失效时间，单位
        return new QRCode(code,file);
    }

	//根据唯一标识，查询用户是否登录成功
    @Override
    public FaceLoginResult checkQRCode(String code) {
        String key = getCacheKey(code);
        return (FaceLoginResult) redisTemplate.opsForValue().get(key);
    }

	//扫描二维码之后，使用拍摄照片进行登录
    @Override
    public String loginByFace(String code, MultipartFile attachment) throws Exception {
        String encode = Base64Util.encode(attachment.getBytes());
        String userID = aiUtil.faceSearch(encode);
        FaceLoginResult result=new FaceLoginResult("0");
        if (userID!=null){
            //自己模拟登录
            User user=userDao.findById(userID).get();
            if (user!=null){
                Subject subject = SecurityUtils.getSubject();
                subject.login(new UsernamePasswordToken(user.getMobile(),user.getPassword()));
                String token = subject.getSession().getId()+"";
                result=new FaceLoginResult("1",token,userID);
            }
        }
        redisTemplate.boundValueOps(getCacheKey(code)).set(result,10,TimeUnit.MINUTES);
        return userID;
    }

	//构造缓存key
    private String getCacheKey(String code) {
        return "qrcode_" + code;
    }
}
