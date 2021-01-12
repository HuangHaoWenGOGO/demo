package seassoon.rule.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import seassoon.rule.dto.query.PermissionQuery;
import seassoon.rule.entity.Permission;
import seassoon.rule.service.PermissionService;
import seassoon.rule.utils.CopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
@Api(tags = "permission")
@RestController
@RequestMapping("/permission")
public class PermissionController {


    @Autowired
    private PermissionService permissionService;

    @ApiOperation("权限列表")
    @GetMapping
    public Page<Permission> query(PermissionQuery permissionQuery) {
        return permissionService.query(permissionQuery);
    }


    @GetMapping("{id}")
    public Permission get(@PathVariable Integer id) {
        return permissionService.getById(id);
    }


    @PostMapping
    public Permission save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return permission;
    }

    @ApiOperation("修改")
    @PutMapping("{id}")
    public Permission update(@PathVariable Integer id, @RequestBody Permission permission) {


        Permission dbEentity = permissionService.getById(id);
        CopyUtils.copyProperties(permission, dbEentity);
        Boolean result = permissionService.updateById(dbEentity);


        return dbEentity;

    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        permissionService.removeById(id);
    }


}
