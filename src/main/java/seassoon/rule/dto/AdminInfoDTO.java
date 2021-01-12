package seassoon.rule.dto;

import seassoon.rule.entity.Permission;
import seassoon.rule.entity.Role;
import lombok.Data;

import java.util.List;

/**
 * @Auther: zhangqianfeng
 * @Date: 2019/10/18 10:54
 * @Description:
 */
@Data
public class AdminInfoDTO {

    private Integer id;

    private String username;

    private String password;


    private String email;


    private String telphone;

    private Integer state;


    private List<Role> roles;

    private List<Permission> permissions;







}
