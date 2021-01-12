package seassoon.rule.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DataGroupVO {
	
	private Integer id;

    private String name;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
    
    private String pubName;

    private Integer pubAdminId;
    
}
