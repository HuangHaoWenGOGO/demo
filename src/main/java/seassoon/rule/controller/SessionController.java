package seassoon.rule.controller;


import seassoon.rule.dto.AdminInfoDTO;
import seassoon.rule.entity.Admin;
import seassoon.rule.exception.CustomException;
import seassoon.rule.exception.CustomExceptionMessage;

import seassoon.rule.service.AdminService;
import seassoon.rule.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author 张前峰
 * @Date: 2018/11/20 10:48
 * @Description: token 控制器
 */
@RestController
@RequestMapping("/sessions")
@Api(tags = {"session"})
public class SessionController {

    private static Logger logger = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    private AdminService adminService;


    @GetMapping
    public AdminInfoDTO status() {

// 获取主体
        Subject subject = SecurityUtils.getSubject();

        if (!SecurityUtils.getSubject().isAuthenticated()) {
            throw new CustomException.AuthenticationException(CustomExceptionMessage.USER_AUTHENTICATION);
        } else {
            Admin admin = (Admin) subject.getPrincipal();

            return adminService.getAdminInfo(admin.getId());
        }


    }


    @ApiOperation(value = "登录")
    @PostMapping("login")
    public Object login(String username,
                        String password, HttpServletRequest request, HttpServletResponse response) {


        // 表面校验
        if (!StringUtils.isNotBlank(username)) {
            throw new CustomException.BadRequestException(CustomExceptionMessage.USERNAME_IS_EMPTY);
        }
        if (!StringUtils.isNotBlank(password)) {
            throw new CustomException.BadRequestException(CustomExceptionMessage.PASSWORD_IS_EMPTY);
        }


        String encryptPassword = DigestUtils.md5DigestAsHex(password.getBytes());


        // 获取主体
        Subject subject = SecurityUtils.getSubject();
        try {
            // 调用安全认证框架的登录方法
            subject.login(new UsernamePasswordToken(username, encryptPassword));

            Admin admin = adminService.findByUsername(username);

            Map<String, Object> resultMap = new HashMap<>();

            String newToken = JwtUtil.sign(username);
            resultMap.put("token", newToken);

            resultMap.put("admin", admin);
            return resultMap;

        } catch (AuthenticationException ex) {
            logger.error("用户名或密码错误", ex);
            throw new CustomException.BadRequestException(CustomExceptionMessage.USERNAME_OR_PASSWORD_ERROR_DOES_NOT_MATCH);
        } catch (UnsupportedEncodingException e) {
            logger.error("服务器异常", e);
            throw new CustomException.ServiceException("服务器异常");
        }


    }


    @ApiOperation(value = "注销")
    @DeleteMapping
    public void logout() {

        Subject subject = SecurityUtils.getSubject();
        subject.logout();

    }


    @PutMapping("/password")
    public void changePassword(String oldPassword, String newPassword) {

        adminService.changePassword(oldPassword, newPassword);


    }


}
