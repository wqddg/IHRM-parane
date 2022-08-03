package com.wqddg.common.shiro.session.sessionManager;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @Author: wqddg
 * @ClassName CurotmSessionManager
 * @DateTime: 2022/1/7 23:19
 * @remarks : # 会话管理   自定义的sessionManager
 */
public class CurotmSessionManager extends DefaultWebSessionManager {

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id= WebUtils.toHttp(request).getHeader("Authorization");
        if (StringUtils.isEmpty(id)){
            //如果没有携带请求头可以生成
            return super.getSessionId(request,response);
        }else {
            id = id.replaceAll("Bearer ","");
            //返回sessionId    固定的
            //请求头的数据
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,"header");
            //id为
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,id);
            //是否需要验证
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,Boolean.TRUE);
            return id;
        }
    }
}
