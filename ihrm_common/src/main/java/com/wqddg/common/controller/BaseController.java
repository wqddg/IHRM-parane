package com.wqddg.common.controller;

import com.wqddg.domain.system.response.ProfileResult;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wqddg
 * @ClassName BaseController
 * @DateTime: 2022/1/4 15:15
 * @remarks : #
 */
public class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String userId;
    protected String companyName;

//使用jwt方式获取
//    @ModelAttribute
//    public void setResAnReq(HttpServletRequest request,HttpServletResponse response){
//        this.request=request;
//        this.response=response;
//
//        Object obj=request.getAttribute("user_claims");
//        if (obj!=null){
//            this.claims= (Claims) obj;
//            this.companyId= (String) claims.get("companyId");
//            this.companyName= (String) claims.get("companyName");
//        }
//    }

    @ModelAttribute
    public void setResAnReq(HttpServletRequest request,HttpServletResponse response){
        this.request=request;
        this.response=response;
        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //1.subject获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        if (principals!=null && !principals.isEmpty()){
            ProfileResult profileResult = (ProfileResult) principals.getPrimaryPrincipal();
            this.companyId= profileResult.getCompanyId();
            this.companyName= profileResult.getCompany();
            this.userId = profileResult.getUserId();
        }
    }
}
