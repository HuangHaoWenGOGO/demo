package seassoon.rule.vo;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataSetVO {
	
	private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "数据组id")
    private Integer dataGroupId;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
    
    private Integer pubAdminId;
    
    private String pubName;
    
    private Integer dataStandardNumber;
    
    private List<DataSetReviewerConfigVO>  dataSetReviewerConfigList;
    
    
}
