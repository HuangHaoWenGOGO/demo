package seassoon.rule.controller;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import seassoon.rule.dto.query.SysLogPageQuery;
import seassoon.rule.entity.SysLog;
import seassoon.rule.es.EsDemoService;

/**
 * <p>
 * ES日志查询，统计
 * </p>
 *
 * @author FW
 * @since 2020-84-19
 */
//@Api(tags = "sysLogDemo")
//@RestController
//@RequestMapping("/syslogDemo")
public class SysLogDemoController {

	@Autowired
	EsDemoService esDemoService;
	
	@GetMapping("test")
    public void get() throws Exception {
		
		
		SearchResponse response = esDemoService.fuzzyQuey("app-demo-yunying", "fuwei", "username");
		System.out.println(JSON.toJSONString(response));
		
		SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
        	System.out.println(hit.getSourceAsString());
        }
        
        
        

		
    }
	
	@GetMapping("date")
    public void date() throws Exception {
		
		
		SearchResponse response = esDemoService.search("app-demo-yunying", "2020-08-20 00:00:00", "2020-08-21 00:00:00");
		System.out.println(JSON.toJSONString(response));
		
		SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
        	System.out.println(hit.getSourceAsString());
        }
        
    }
	
	@GetMapping("query")
    public void query(SysLogPageQuery sysLogPageQuery) throws Exception {
		
		SearchResponse response = esDemoService.searchQuery("app-demo-yunying", sysLogPageQuery);
		System.out.println(JSON.toJSONString(response));
		
		SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
        	System.out.println(hit.getSourceAsString());
        }
        
        
    }
	
	@GetMapping("page")
    public Page<SysLog> page(SysLogPageQuery sysLogPageQuery) throws Exception {
		
		Page<SysLog> pgob = esDemoService.searchPageQuery("app-demo-yunying", sysLogPageQuery);
        
        return pgob;
    }
	
	@GetMapping("aggdemo")
    public String aggdemo(SysLogPageQuery sysLogPageQuery) throws Exception {
		
		SearchResponse response = esDemoService.getAggregationDemo("app-demo-yunying",sysLogPageQuery);
		//System.out.println(JSON.toJSONString(response));
        return response.toString();
    }
	
}
