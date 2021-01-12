package seassoon.rule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import seassoon.rule.dto.AdminInfoDTO;
import seassoon.rule.dto.AdminInputDTO;
import seassoon.rule.dto.query.AdminQuery;
import seassoon.rule.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import seassoon.rule.entity.Permission;
import seassoon.rule.entity.Role;

import java.util.List;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
public interface AdminService extends IService<Admin> {


    Page<Admin> query(AdminQuery adminQuery);

    Admin findByUsername(String username);

    List<Role> findRoles(Integer id);

    Admin save(AdminInputDTO adminInputDTO);

    Admin update(Integer id, AdminInputDTO adminInputDTO);

    AdminInfoDTO getAdminInfo(Integer id);

    List<Permission> findPermissions(Integer id);


    void updateRoles(Integer id, List<Integer> roleIds);


    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     */
    void changePassword(String oldPassword, String newPassword);

}
