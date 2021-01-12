package seassoon.rule.mapper;

import seassoon.rule.entity.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    @Delete("delete from ds_role_permission where role_id=#{roleId}")
    int deleteByRoleId(Integer roleId);
    @Delete("delete from ds_role_permission where permission_id=#{permissionId}")
    int deleteByPermissionId(Integer permissionId);

}
