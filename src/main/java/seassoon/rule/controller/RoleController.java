package seassoon.rule.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import seassoon.rule.dto.query.RoleQuery;
import seassoon.rule.entity.Permission;
import seassoon.rule.entity.Role;
import seassoon.rule.service.RoleService;
import seassoon.rule.utils.CopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
@Api(tags = "role")
@RestController
@RequestMapping("/role")
public class RoleController {


    @Autowired
    private RoleService roleService;

    @ApiOperation("角色列表")
    @GetMapping
    public Page<Role> query(RoleQuery roleQuery) {
        return roleService.query(roleQuery);
    }

    @GetMapping("{id}")
    public Role getRole(@PathVariable Integer id) {
        return roleService.getById(id);
    }


    @PostMapping
    public void save(@RequestBody Role role) {
        roleService.save(role);
    }

    @ApiOperation("修改")
    @PutMapping("{id}")
    public Role update(@PathVariable Integer id, @RequestBody Role role) {


        Role dbEentity = roleService.getById(id);
        CopyUtils.copyProperties(role, dbEentity);
        Boolean result = roleService.updateById(dbEentity);


        return dbEentity;

    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        roleService.removeById(id);
    }


    @GetMapping("{id}/permissions")
    public List<Permission> findPermissions(@PathVariable Integer id) {
        return roleService.findPermissions(id);
    }


    @PutMapping("{id}/permissions")
    public void updateRolePermissions(@PathVariable Integer id, @RequestBody List<Integer> permissionIds) {
        roleService.updateRolePermissions(id, permissionIds);
    }

}
