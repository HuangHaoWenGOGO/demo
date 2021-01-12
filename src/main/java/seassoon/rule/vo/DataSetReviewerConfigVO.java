package seassoon.rule.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataSetReviewerConfigVO {

	private Integer id;

	@ApiModelProperty(value = "关联数据标准集id")
	private Integer dataSetId;

	@ApiModelProperty(value = "审核人id")
	private Integer adminId;

	@ApiModelProperty(value = "审核人顺序")
	private Integer reviewOrder;

	private LocalDateTime gmtCreate;

	private LocalDateTime gmtModified;
	
	private String pubName;
	
	private Integer dataStandardId;
	
	

}
