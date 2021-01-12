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
 * 数据标准审核表
 * </p>
 *
 * @author Wayne
 * @since 2020-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ds_data_standard_review_record")
@ApiModel(value="DataStandardReviewRecord对象", description="数据标准审核表")
public class DataStandardReviewRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联数据标准id")
    private Integer dataStandardId;

    @ApiModelProperty(value = "审核状态 审核中，通过，不通过")
    private Integer status;

    @ApiModelProperty(value = "审核层级 按审核人数量计算")
    private Integer reviewLevel;

    @ApiModelProperty(value = "json格式化审核状态")
    private String reviewConfig;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "当前审核人id")
    private Integer currentAdminId;

    @ApiModelProperty(value = "当前审核人顺序")
    private Integer currentReviewerOrder;
    
    @ApiModelProperty(value = "发布的审核人id")
    private Integer pubAdminId;

    @ApiModelProperty(value = "驳回理由")
    private String reviewReason;

}
