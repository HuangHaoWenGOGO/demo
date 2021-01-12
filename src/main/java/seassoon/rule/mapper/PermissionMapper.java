package seassoon.rule.mapper;

import seassoon.rule.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select(value = "select permission.* from ds_permission permission,ds_role_permission role_permission,ds_role role  where permission.id=role_permission.permission_id and role_permission.role_id=role.id and role.id=#{roleId}")
    List<Permission> findByRoleId(Integer roleId);


//    @Select(value = "select permission.* from permission ,role_permission,role  where permission.id=role_permission.permission_id and role_permission.role_id=role.id and role.id in (${roleIds} )")
    List<Permission> findByRoleIdIn(@Param("roleIds")List<String> roleIds);


}
