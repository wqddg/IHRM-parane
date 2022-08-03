package com.wqddg.system.shiro.realm;

import com.wqddg.common.shiro.realm.IhrmRealm;
import com.wqddg.domain.system.Permission;
import com.wqddg.domain.system.User;
import com.wqddg.domain.system.response.ProfileResult;
import com.wqddg.system.service.PermissionService;
import com.wqddg.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName OhrmRealm
 * @DateTime: 2022/1/7 23:29
 * @remarks : #
 */
public class UserRealm extends IhrmRealm {
    @Override
    public void setName(String name) {
        super.setName("sys");
    }
    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        String username=token.getUsername();
        String password=new String(token.getPassword());
        User user = userService.findMobile(username);
        if (user!=null && user.getPassword().equals(password)){
            //4.构造安全数据并返回（安全数据：用户基本数据，权限信息 profileResult）
            ProfileResult result = null;
            if("user".equals(user.getLevel())) {
                result = new ProfileResult(user);
            }else {
                Map map = new HashMap();
                if("coAdmin".equals(user.getLevel())) {
                    map.put("enVisible","1");
                }
                List<Permission> list = permissionService.finaAll(map);
                result = new ProfileResult(user,list);
            }
            //构造方法：安全数据，密码，realm域名
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(result,user.getPassword(),this.getName());
            return info;
        }
        return null;
    }
}
