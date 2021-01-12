package seassoon.rule.dto.query;

import lombok.Data;

/**
 * 
 * @author waynefu
 *
 */
@Data 
public class DataSetQuery extends  BaseQuery{

	private String keyword;
	
	private Integer dataGroupId;
	
}
