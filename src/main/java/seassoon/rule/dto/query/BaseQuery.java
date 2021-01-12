package seassoon.rule.dto.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询
 *
 * @Auther: zhangqianfeng
 * @Date: 2020/4/24 13:40
 * @Description:
 */
@Data
public class BaseQuery {

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    @ApiModelProperty("按属性排序,格式:属性(| asc或desc)")
    private List<String> orders = new ArrayList<>();

    @ApiModelProperty(hidden = true)
    public Page page() {

        Page page = new Page(pageNo, pageSize);

        List<OrderItem> orderItems = new ArrayList<>();


        for (String orderStr : orders) {

            String[] sort = orderStr.split("\\|");
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(sort[0]);
            orderItem.setAsc("asc".equals(sort[1].toLowerCase()) ? true : false);
            orderItems.add(orderItem);
        }

        page.setOrders(orderItems);

        return page;

    }


}
