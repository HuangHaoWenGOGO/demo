package seassoon.rule.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据标准审核人配置表
 * </p>
 *
 * @author Wayne
 * @since 2020-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ds_data_set_reviewer_config")
@ApiModel(value="DataSetReviewerConfig对象", description="数据标准审核人配置表")
public class DataSetReviewerConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联数据标准集id")
    private Integer dataSetId;

    @ApiModelProperty(value = "审核人id")
    private Integer adminId;

    @ApiModelProperty(value = "审核人顺序")
    private Integer reviewOrder;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
    
    private Integer dataStandardId;


}
