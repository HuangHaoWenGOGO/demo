package seassoon.rule.controller;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import seassoon.rule.dto.query.SysLogPageQuery;
import seassoon.rule.entity.SysLog;
import seassoon.rule.service.SysLogService;

/**
 * <p>
 * ES日志查询，统计
 * </p>
 *
 * @author FW
 * @since 2020-84-19
 */
@Api(tags = "sysLog")
@RestController
@RequestMapping("/syslog")
public class SysLogController {

	@Autowired
	SysLogService sysLogService;
	
	@Value("${elasticsearch.index}")
	private String esIndex;
	
	
	@GetMapping("page")
    public Page<SysLog> page(SysLogPageQuery sysLogPageQuery) throws Exception {
		
		Page<SysLog> pgob = sysLogService.searchSysLogPageQuery(esIndex, sysLogPageQuery);
        
        return pgob;
    }
	
	@GetMapping("aggsyslog")
    public String aggdemo(SysLogPageQuery sysLogPageQuery) throws Exception {
		
		SearchResponse response = sysLogService.aggregationSysLog(esIndex,sysLogPageQuery);
		//System.out.println(JSON.toJSONString(response));
        return response.toString();
    }
	
}
