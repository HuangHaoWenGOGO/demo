package seassoon.rule.mapper;

import seassoon.rule.entity.AdminRole;
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
public interface AdminRoleMapper extends BaseMapper<AdminRole> {



    /**
     * 根据管理员删除
     * @param adminId
     * @return
     */
    @Delete("delete from  ds_admin_role where admin_id=#{adminId} ")
    int deleteByAdminId(Integer adminId);


    /**
     * 根据角色删除
     * @param roleId
     * @return
     */
    int deleteByRoleId(Integer roleId);

}
