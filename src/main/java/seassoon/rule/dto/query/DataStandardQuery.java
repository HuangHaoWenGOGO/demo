package seassoon.rule.dto.query;

import lombok.Data;

@Data
public class DataStandardQuery extends  BaseQuery{

	private String keyword;
	
	private Integer dataSetId;
	
}
