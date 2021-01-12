package seassoon.rule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import seassoon.rule.dto.query.RoleQuery;
import seassoon.rule.entity.Permission;
import seassoon.rule.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
public interface RoleService extends IService<Role> {
    Role findByName(String name);

    List<Permission> findPermissions(Integer id);


    void updateRolePermissions(Integer id, List<Integer> permissionIds);


    Page<Role> query(RoleQuery roleQuery);


}
