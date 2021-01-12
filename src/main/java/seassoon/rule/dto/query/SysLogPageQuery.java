package seassoon.rule.dto.query;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SysLogPageQuery {
	
	@NotNull
	private String systemName;
	@NotNull
	private Integer pageNum;
	
	@NotNull
	private Integer pageSize;
	
	private String startDate;
	
	private String endDate;
	
	/**
	 * 请求类型 ： 查询、修改 等等
	 */
	private String requestType;
	
	/**
	 * 用户名
	 */
	private String account;
	
	private String level;
	
	private String platform;

}
