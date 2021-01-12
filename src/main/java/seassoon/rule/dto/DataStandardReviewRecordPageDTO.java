package seassoon.rule.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import seassoon.rule.entity.DataStandardAppendFile;

@Data
public class DataStandardReviewRecordPageDTO {
	
    private Integer id;

    private Integer dataStandardId;

    private Integer status;

    private Integer reviewLevel;

    private String reviewConfig;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Integer currentAdminId;

    private Integer currentReviewerOrder;
    
    private Integer pubAdminId;
    
    private String name;
    
    private List<DataStandardAppendFile> DataStandardAppendFileList;

}
