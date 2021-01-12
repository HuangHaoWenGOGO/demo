package seassoon.rule.dto;

import seassoon.rule.constants.StateEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * @Auther: zhangqianfeng
 * @Date: 2019/10/18 10:54
 * @Description:
 */
@Data
public class AdminInputDTO {


    private String username;

    private String password;


    @NotNull(message = "email必须填")
    private String email;

    @NotNull(message = "手机号必须填")
    @Pattern(regexp = "^[1][0-9][0-9]{9}$", message = "请输入11位手机号") // 手机号
    private String telphone;

    private StateEnum state;

    private Set<Integer> roleIds;


}
