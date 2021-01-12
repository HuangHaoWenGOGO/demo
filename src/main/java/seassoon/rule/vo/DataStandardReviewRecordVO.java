package seassoon.rule.vo;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import seassoon.rule.entity.DataStandardAppendFile;

@Data
public class DataStandardReviewRecordVO {

	private Integer id;

	private Integer dataStandardId;

	private Integer status;

	private Integer reviewLevel;

	private LocalDateTime gmtCreate;

	private LocalDateTime gmtModified;

	private Integer currentReviewerOrder;

	private String dataStandardName;
	
	private String dataSetName;
	
	private String version;
	
	@ApiModelProperty(value = "当前审核人id")
	private Integer currentAdminId;
	
	private String currentAdminName;
	
	private String pubAdminName;
	
	@ApiModelProperty(value = "发布的审核人id")
    private Integer pubAdminId;

	private List<DataStandardAppendFile> dataStandardAppendFileList;
	
	private Integer dataSetId;

}
