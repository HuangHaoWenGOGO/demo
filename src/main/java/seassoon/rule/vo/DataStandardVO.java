package seassoon.rule.vo;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import seassoon.rule.entity.DataStandardAppendFile;

@Data
public class DataStandardVO {

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
    
    private String pubName;
    
    private List<DataStandardAppendFile> dataStandardAppendFileList;
    
    private List<DataSetReviewerConfigVO>  dataSetReviewerConfigList;
    
}
