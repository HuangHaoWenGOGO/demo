package seassoon.rule.shiro;

import seassoon.rule.entity.Admin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 获取当前登录管理员信息
 */
public class ShiroUtils {

    public static Admin currentAdmin() {

        Subject subject = SecurityUtils.getSubject();


        Admin admin = (Admin) subject.getPrincipal();

        return admin;

    }

    public static Integer currentAdminId() {

        Subject subject = SecurityUtils.getSubject();


        Admin admin = (Admin) subject.getPrincipal();

        return admin.getId();

    }

    public static String currentAdminName() {

        Subject subject = SecurityUtils.getSubject();


        Admin admin = (Admin) subject.getPrincipal();

        return admin.getUsername();

    }

    public static Boolean checkRoles() {


        return false;

    }

    public static void checkEditorRole() {

        Subject subject = SecurityUtils.getSubject();

        subject.checkRoles("ADMIN");

    }


}
