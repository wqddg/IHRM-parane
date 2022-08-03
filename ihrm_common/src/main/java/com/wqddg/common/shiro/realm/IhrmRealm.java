package com.wqddg.common.shiro.realm;

import com.wqddg.domain.system.response.ProfileResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * @Author: wqddg
 * @ClassName IhrmRealm
 * @DateTime: 2022/1/7 23:26
 * @remarks : # 公共的realm 获取安全数据   ，构造权限信息
 */
public class IhrmRealm extends AuthorizingRealm {

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ProfileResult primaryPrincipal = (ProfileResult) principalCollection.getPrimaryPrincipal();
        Set<String> apis = (Set<String>) primaryPrincipal.getRoles().get("apis");
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.setStringPermissions(apis);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
