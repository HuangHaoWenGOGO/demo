package seassoon.rule.mapper;

import seassoon.rule.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import seassoon.rule.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {


    @Select("select role.id,role.name,role.comment from ds_admin admin,ds_admin_role admin_role,ds_role role  where admin.id=admin_role.admin_id and admin_role.role_id=role.id and admin.id=#{adminId}")
    List<Role> findRoleByAdminId(Integer adminId);





}
