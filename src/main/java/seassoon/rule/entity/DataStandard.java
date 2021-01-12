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
 * 数据标准表
 * </p>
 *
 * @author Wayne
 * @since 2020-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ds_data_standard")
@ApiModel(value="DataStandard对象", description="数据标准表")
public class DataStandard implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "数据标准名称")
    private String name;

    @ApiModelProperty(value = "版本名称")
    private String version;

    @ApiModelProperty(value = "数据组id")
    private Integer dataGroupId;

    @ApiModelProperty(value = "数据标准集id")
    private Integer dataSetId;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
    
    private int status;
    
    private Integer pubAdminId;


}
