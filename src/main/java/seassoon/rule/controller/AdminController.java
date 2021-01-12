package seassoon.rule.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import seassoon.rule.dto.AdminInputDTO;
import seassoon.rule.dto.query.AdminQuery;
import seassoon.rule.entity.Admin;
import seassoon.rule.entity.Role;
import seassoon.rule.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
@Api(tags = "admin")
@RestController
@RequestMapping("/admin")
public class AdminController {
	
    @Autowired
    private AdminService adminService;

    @ApiOperation("管理员列表")
    @GetMapping
    public Page<Admin> query(AdminQuery adminQuery) {
        return adminService.query(adminQuery);
    }


    @GetMapping("{id}")
    public Admin get(@PathVariable Integer id) {
        return adminService.getById(id);
    }

    @PostMapping
    public Admin save(@Valid  @RequestBody AdminInputDTO adminInputDTO) {
        return adminService.save(adminInputDTO);
    }

    @ApiOperation("修改")
    @PutMapping("{id}")
    public Admin update(@PathVariable Integer id, @Valid @RequestBody AdminInputDTO adminInputDTO) {
        return adminService.update(id, adminInputDTO);
    }


    @GetMapping("{id}/roles")
    public List<Role> findRoles(@PathVariable Integer id) {
        return adminService.findRoles(id);
    }

    @PutMapping("{id}/roles")
    public void updateRoles(@PathVariable Integer id, @RequestBody List<Integer> roleIds) {
        adminService.updateRoles(id, roleIds);
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        adminService.removeById(id);
    }

}
