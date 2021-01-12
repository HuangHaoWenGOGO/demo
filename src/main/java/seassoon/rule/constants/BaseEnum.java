package seassoon.rule.constants;

/**
 * 枚举基类.
 * @author Wang.ch
 *
 * @param <Y> 数据库存储的java类型
 */
public interface BaseEnum<Y> {
    /**
     * 存取到数据库中的值.
     * @return
     */
    public Y getValue();
}
