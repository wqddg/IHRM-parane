package com.wqddg.social;

/**
 * @Author: wqddg
 * @ClassName ShiroConfiguration
 * @DateTime: 2022/1/8 0:03
 * @remarks : #
 */
import com.wqddg.common.shiro.realm.IhrmRealm;
import com.wqddg.common.shiro.session.sessionManager.CurotmSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {
    //1.创建realm
    @Bean
    public IhrmRealm getRealm(){
        return new IhrmRealm();
    }
    //2.安全管理器
    @Bean
    public SecurityManager getSecurityManager(IhrmRealm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        //将自定义的会话管理器注册到安全管理中
        defaultWebSecurityManager.setSessionManager(sessionManager());
        //将自定义的redis缓存器注册到安全管理器中
        defaultWebSecurityManager.setCacheManager(redisCacheManager());
        return defaultWebSecurityManager;
    }

    //3.配置shiro的过滤器工厂
    /**
     * 在web程序中,shiro进行权限控制  全部是通过一组过滤器集合进行控制
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        //1.创建过滤器工厂
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        //2.设置安全管理器
        bean.setSecurityManager(securityManager);
        //3.通过配置(调整登录页面 为授权的页面)
        bean.setLoginUrl("/autherror?code=1");//跳转到url
        bean.setUnauthorizedUrl("/autherror?code=2");//未授权的url
        //4.设置过滤器集合
        Map<String,String> filterMap=new LinkedHashMap<>();
        //具有某种权限才能访问
//        filterMap.put("/user/home","perms[user-home]");
        filterMap.put("/sys/login","anon");//当前请求地址可以匿名访问
        filterMap.put("/autherror","anon");//当前请求地址可以匿名访问
        filterMap.put("/**","authc");//当前请求地址必须认证之后可以访问
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;
    //1.redis的控制器      操作redis
    public RedisManager redisManager(){
        RedisManager redisManager=new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setPassword(password);
        return redisManager;
    }
    //2.sessionDao
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO dao=new RedisSessionDAO();
        dao.setRedisManager(redisManager());
        return dao;
    }
    //3.会话管理器
    public DefaultWebSessionManager sessionManager(){
        CurotmSessionManager sessionManager=new CurotmSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        //禁用cookid
        sessionManager.setSessionIdCookieEnabled(false);
        //禁用url重写  url;jsesssionId=id
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }
    //4.缓存管理器
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager=new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    //配置shiro注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}

