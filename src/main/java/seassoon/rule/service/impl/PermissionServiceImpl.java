package seassoon.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import seassoon.rule.dto.query.PermissionQuery;
import seassoon.rule.entity.Permission;
import seassoon.rule.mapper.PermissionMapper;
import seassoon.rule.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public Permission findByName(String name) {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper();

        queryWrapper.eq("name", name);

        return getOne(queryWrapper);
    }

    @Override
    public Page<Permission> query(PermissionQuery permissionQuery) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();


        if (StringUtils.isNotBlank(permissionQuery.getKeyword())) {
            queryWrapper.eq("name", permissionQuery.getKeyword());
        }


        return this.page(permissionQuery.page(), queryWrapper);


    }
}
