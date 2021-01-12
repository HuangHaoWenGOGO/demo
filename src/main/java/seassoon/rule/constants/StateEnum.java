package seassoon.rule.constants;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @Auther: zhangqianfeng
 * @Date: 2020/4/21 10:50
 * @Description:
 */
public enum StateEnum implements IEnum<Integer> {
    /**
     * 禁用
     */
    DISABLED(0, "禁用"),
    /**
     * 启用
     */
    ENABLED(1, "启用");

    private int code;
    private String comment;

    StateEnum(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public int getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public Integer getValue() {
        return code;
    }

}
