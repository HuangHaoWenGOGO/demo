package seassoon.rule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import seassoon.rule.dto.query.PermissionQuery;
import seassoon.rule.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
public interface PermissionService extends IService<Permission> {
    Permission findByName(String name);


    Page<Permission> query(PermissionQuery permissionQuery);
}
