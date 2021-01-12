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
 * 数据标准关联的文件表
 * </p>
 *
 * @author Wayne
 * @since 2020-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ds_data_standard_append_file")
@ApiModel(value="DataStandardAppendFile对象", description="数据标准关联的文件表")
public class DataStandardAppendFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联数据标准id")
    private Integer dataStandardId;

    @ApiModelProperty(value = "路径")
    private String filePath;

    @ApiModelProperty(value = "文件保存名称")
    private String fileName;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


}
