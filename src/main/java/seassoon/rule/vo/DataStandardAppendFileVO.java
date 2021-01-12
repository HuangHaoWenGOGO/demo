package seassoon.rule.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataStandardAppendFileVO {
	
	
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
    
    private Integer dataSetId;

}
