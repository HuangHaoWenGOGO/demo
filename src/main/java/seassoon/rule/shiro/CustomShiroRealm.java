package seassoon.rule.shiro;

import seassoon.rule.entity.Admin;
import seassoon.rule.entity.Permission;
import seassoon.rule.entity.Role;
import seassoon.rule.service.AdminService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaodong
 * @version v1.0
 * @email zhaodongxx@outlook.com
 * @since 2018/3/30 22:55
 */
public class CustomShiroRealm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(CustomShiroRealm.class);

    @Autowired
    @Lazy
    protected AdminService adminService;


    /**
     * 限定这个Realm只支持我们自定义的JWT Token
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }


    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取用户账号
        String username = principals.toString();

        Admin admin = adminService.findByUsername(username);

        /**
         * 添加角色
         */
        List<Role> roles = adminService.findRoles(admin.getId());
        authorizationInfo.addRoles(roles.stream().map(f -> f.getName()).collect(Collectors.toList()));

        if (roles.size() > 0) {
            List<Permission> permissions = adminService.findPermissions(admin.getId());
            /**
             * 添加权限
             */
            authorizationInfo.addStringPermissions(permissions.stream().map(f -> f.getPermission()).collect(Collectors.toList()));

        }


        return authorizationInfo;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户账号
        String username = token.getPrincipal().toString();

        Admin admin = adminService.findByUsername(username);

        logger.info("loggin  username:" + username);

        if (admin != null) {


            AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    admin,
                    admin.getPassword(),
                    getName());
            return authenticationInfo;
        }


        if (admin == null) {
            throw new AuthenticationException("该用户不存在！");
        }

        return null;
    }
}
