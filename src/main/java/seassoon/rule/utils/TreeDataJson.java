package seassoon.rule.utils;

import java.util.List;

import lombok.Data;


@Data
public class TreeDataJson {

	
	private Integer id;
	
	private String label;
	
	private List<TreeDataJson> children;
	
}
