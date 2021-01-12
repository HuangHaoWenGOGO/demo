package seassoon.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import seassoon.rule.dto.query.RoleQuery;
import seassoon.rule.entity.Permission;
import seassoon.rule.entity.Role;
import seassoon.rule.entity.RolePermission;
import seassoon.rule.mapper.PermissionMapper;
import seassoon.rule.mapper.RoleMapper;
import seassoon.rule.mapper.RolePermissionMapper;
import seassoon.rule.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    private PermissionMapper permissionMapper;


    private RolePermissionMapper rolePermissionMapper;

    @Override
    public Role findByName(String name) {

        QueryWrapper<Role> queryWrapper = new QueryWrapper();

        queryWrapper.eq("name", name);

        return getOne(queryWrapper);
    }

    @Override
    public List<Permission> findPermissions(Integer id) {
        return permissionMapper.findByRoleId(id);
    }

    @Override
    public void updateRolePermissions(Integer id, List<Integer> permissionIds) {

        rolePermissionMapper.deleteByRoleId(id);

        for (Integer permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(id);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);

        }

    }

    @Override
    public Page<Role> query(RoleQuery roleQuery) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();


        if (StringUtils.isNotBlank(roleQuery.getKeyword())) {
            queryWrapper.eq("name", roleQuery.getKeyword());
        }


        return this.page(roleQuery.page(), queryWrapper);
    }
}
