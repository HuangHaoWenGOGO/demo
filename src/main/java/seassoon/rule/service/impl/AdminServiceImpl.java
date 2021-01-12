package seassoon.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import seassoon.rule.converter.AdminConverter;
import seassoon.rule.dto.AdminInfoDTO;
import seassoon.rule.dto.AdminInputDTO;
import seassoon.rule.dto.query.AdminQuery;
import seassoon.rule.entity.Admin;
import seassoon.rule.entity.AdminRole;
import seassoon.rule.entity.Permission;
import seassoon.rule.entity.Role;
import seassoon.rule.exception.CustomException;
import seassoon.rule.exception.CustomExceptionMessage;
import seassoon.rule.mapper.AdminMapper;
import seassoon.rule.mapper.AdminRoleMapper;
import seassoon.rule.mapper.PermissionMapper;
import seassoon.rule.mapper.RoleMapper;
import seassoon.rule.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import seassoon.rule.shiro.ShiroUtils;
import seassoon.rule.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author zhangqianfeng
 * @since 2020-04-24
 */
@Service

public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminConverter adminConverter;

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Page<Admin> query(AdminQuery adminQuery) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(adminQuery.getKeyword())) {
            queryWrapper.like("username", adminQuery.getKeyword());
        }


        return this.page(adminQuery.page(), queryWrapper);

    }

    @Override
    public Admin findByUsername(String username) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Role> findRoles(Integer id) {


        return baseMapper.findRoleByAdminId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Admin save(AdminInputDTO adminInputDTO) {

        Admin admin = adminConverter.convertToAdmin(adminInputDTO);


        Admin dbEntity = findByUsername(admin.getUsername());

        if (dbEntity != null) {
            throw new CustomException.ConflictException(CustomExceptionMessage.USERNAME_ALREADY_EXIST);
        }


        admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));


        boolean result = save(admin);

        Set<Integer> roleIds = adminInputDTO.getRoleIds();
        if (roleIds == null) {
            throw new CustomException.BadRequestException(CustomExceptionMessage.ROLE_ID_IS_EMPTY);
        }


        for (Integer roleId : roleIds) {
            Role role = roleMapper.selectById(roleId);
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(admin.getId());
            adminRole.setRoleId(roleId);

            adminRoleMapper.insert(adminRole);

        }


        return admin;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Admin update(Integer id, AdminInputDTO adminInputDTO) {

        Admin admin = adminConverter.convertToAdmin(adminInputDTO);


        Admin dbEntity = getById(id);

        Admin checkEntity = findByUsername(admin.getUsername());

        if (checkEntity != null && !checkEntity.getId().equals(id)) {
            throw new CustomException.ConflictException(CustomExceptionMessage.USERNAME_ALREADY_EXIST);
        }

        if(org.apache.commons.lang3.StringUtils.isNotBlank(admin.getPassword())){
            admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
        }
        CopyUtils.copyProperties(admin, dbEntity);

        baseMapper.updateById(dbEntity);


        /**
         * 设置角色
         */
        adminRoleMapper.deleteByAdminId(id);

        Set<Integer> roleIds = adminInputDTO.getRoleIds();
        for (Integer roleId : roleIds) {
            Role role = roleMapper.selectById(roleId);
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(id);
            adminRole.setRoleId(roleId);
            adminRoleMapper.insert(adminRole);
        }


        return dbEntity;
    }

    @Override
    public AdminInfoDTO getAdminInfo(Integer id) {
        Admin admin = this.getById(id);

        List<Role> roles = baseMapper.findRoleByAdminId(id);

        List<Permission> permissions = this.findPermissions(id);

        AdminInfoDTO adminInfoDTO = new AdminInfoDTO();

        CopyUtils.copyProperties(admin, adminInfoDTO);


        adminInfoDTO.setRoles(roles);
        adminInfoDTO.setPermissions(permissions);

        return adminInfoDTO;
    }

    @Override
    public List<Permission> findPermissions(Integer id) {
//        Admin admin = this.getById(id);

        List<Role> roles = baseMapper.findRoleByAdminId(id);

        List<Permission> permissions = null;
        if (!CollectionUtils.isEmpty(roles)) {
            List<Integer> roleIds = roles.stream().map(f -> f.getId()).collect(Collectors.toList());

            permissions = permissionMapper.findByRoleIdIn(roleIds.stream().map(f->f.toString()).collect(Collectors.toList()));
        }

        return permissions;



    }

    @Override
    public void updateRoles(Integer id, List<Integer> roleIds) {

        /**
         * 设置角色
         */
        adminRoleMapper.deleteByAdminId(id);


        for (Integer roleId : roleIds) {
            Role role = roleMapper.selectById(roleId);
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(id);
            adminRole.setRoleId(roleId);
            adminRoleMapper.insert(adminRole);
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

        /**
         * 确认旧密码正确
         */
        Admin user = this.findByUsername(ShiroUtils.currentAdminName());

        String encryptionOldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());

        if (!user.getPassword().equals(encryptionOldPassword)) {
            throw new CustomException.BadRequestException(CustomExceptionMessage.CURRENT_PASSWORD_ERROR);
        }

        /**
         *更新密码
         */
        String encryptionNewPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());

        user.setPassword(encryptionNewPassword);

        baseMapper.updateById(user);

    }
}
