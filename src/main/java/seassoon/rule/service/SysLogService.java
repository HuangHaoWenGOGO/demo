package seassoon.rule.service;

import org.elasticsearch.action.search.SearchResponse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import seassoon.rule.dto.query.SysLogPageQuery;
import seassoon.rule.entity.SysLog;

public interface SysLogService {
	
	public Page<SysLog> searchSysLogPageQuery(String index, SysLogPageQuery sysLogPageQuery) throws Exception;
	
	public SearchResponse aggregationSysLog(String indexName, SysLogPageQuery sysLogPageQuery) throws Exception;

}
