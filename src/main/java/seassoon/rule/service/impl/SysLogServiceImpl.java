package seassoon.rule.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.AllArgsConstructor;
import seassoon.rule.dto.query.SysLogPageQuery;
import seassoon.rule.entity.SysLog;
import seassoon.rule.service.SysLogService;
import seassoon.rule.utils.StringUtils;

@Service
@AllArgsConstructor
public class SysLogServiceImpl implements SysLogService {

	@Autowired
	private RestHighLevelClient client;

	public Page<SysLog> searchSysLogPageQuery(String index, SysLogPageQuery sysLogPageQuery) throws Exception {

		Integer page = sysLogPageQuery.getPageNum() <= 0 ? 0 : sysLogPageQuery.getPageNum() - 1;
		Integer pageSize = sysLogPageQuery.getPageSize();

		String username = sysLogPageQuery.getAccount();
		String requestType = sysLogPageQuery.getRequestType();

		String level = sysLogPageQuery.getLevel();
		String platform = sysLogPageQuery.getPlatform();

		// init ES query
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().size(pageSize).from(page * pageSize)
				.sort("record_time", SortOrder.DESC);
		SearchRequest searchRequest = new SearchRequest(index);

		if (StringUtils.isNotBlank(username)) {
			booleanQueryBuilder.must(QueryBuilders.wildcardQuery("username", "*" + username + "*"));
		}

		if (StringUtils.isNotBlank(requestType)) {
			booleanQueryBuilder.must(QueryBuilders.matchQuery("requestType.keyword", requestType));
		}

		if (StringUtils.isNotBlank(level)) {
			booleanQueryBuilder.must(QueryBuilders.matchQuery("level.keyword", level));
		}

		if (StringUtils.isNotBlank(platform)) {
			booleanQueryBuilder.must(QueryBuilders.matchQuery("platform.keyword", platform));
		}

		if (StringUtils.isNotBlank(sysLogPageQuery.getStartDate())
				&& StringUtils.isBlank(sysLogPageQuery.getEndDate())) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = dateFormat.parse(sysLogPageQuery.getStartDate());
			booleanQueryBuilder.must(QueryBuilders.rangeQuery("@timestamp").from(startDate));
		}
		if (StringUtils.isBlank(sysLogPageQuery.getStartDate())
				&& StringUtils.isNotBlank(sysLogPageQuery.getEndDate())) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date endDate = dateFormat.parse(sysLogPageQuery.getEndDate());
			booleanQueryBuilder.must(QueryBuilders.rangeQuery("@timestamp").to(endDate));
		}
		if (StringUtils.isNotBlank(sysLogPageQuery.getStartDate())
				&& StringUtils.isNotBlank(sysLogPageQuery.getEndDate())) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = dateFormat.parse(sysLogPageQuery.getStartDate());
			Date endDate = dateFormat.parse(sysLogPageQuery.getEndDate());
			booleanQueryBuilder.must(QueryBuilders.rangeQuery("@timestamp").from(startDate).to(endDate));
		}

		sourceBuilder.query(booleanQueryBuilder);
		searchRequest.source(sourceBuilder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		Page<SysLog> pageOb = new Page<SysLog>();

		SearchHits hits = response.getHits();
		SearchHit[] searchHits = hits.getHits();
		Long total = hits.getTotalHits();
		List<SysLog> sysLogList = new ArrayList<SysLog>();
		for (SearchHit hit : searchHits) {
			SysLog sysLog = new SysLog();
			sysLog.setEsId(hit.getId());
			sysLog.setPlatform(
					hit.getSourceAsMap().containsKey("platform") ? hit.getSourceAsMap().get("platform").toString()
							: "");
			sysLog.setClassName(
					hit.getSourceAsMap().containsKey("class") ? hit.getSourceAsMap().get("class").toString() : "");
			sysLog.setIp(hit.getSourceAsMap().containsKey("ip") ? hit.getSourceAsMap().get("ip").toString() : "");
			sysLog.setLevel(
					hit.getSourceAsMap().containsKey("level") ? hit.getSourceAsMap().get("level").toString() : "");
			sysLog.setLogPath(
					hit.getSourceAsMap().containsKey("source") ? hit.getSourceAsMap().get("source").toString() : "");
			sysLog.setMessage(
					hit.getSourceAsMap().containsKey("message") ? hit.getSourceAsMap().get("message").toString() : "");
			sysLog.setPath(hit.getSourceAsMap().containsKey("path") ? hit.getSourceAsMap().get("path").toString() : "");
			sysLog.setRecordTime(
					hit.getSourceAsMap().containsKey("record_time") ? hit.getSourceAsMap().get("record_time").toString()
							: "");
			sysLog.setRequestMethod(hit.getSourceAsMap().containsKey("requestMethod")
					? hit.getSourceAsMap().get("requestMethod").toString()
					: "");
			sysLog.setRequestType(
					hit.getSourceAsMap().containsKey("type") ? hit.getSourceAsMap().get("type").toString() : "");
			sysLog.setUseragent(
					hit.getSourceAsMap().containsKey("useragent") ? hit.getSourceAsMap().get("useragent").toString()
							: "");
			sysLog.setUsername(
					hit.getSourceAsMap().containsKey("username") ? hit.getSourceAsMap().get("username").toString()
							: "");

			sysLogList.add(sysLog);
		}
		pageOb.setRecords(sysLogList);
		pageOb.setTotal(total);
		pageOb.setSize(pageSize);
		pageOb.setCurrent(sysLogPageQuery.getPageNum());

		return pageOb;
	}

	public SearchResponse aggregationSysLog(String indexName, SysLogPageQuery sysLogPageQuery) throws Exception {

		SearchRequest searchRequest = new SearchRequest(indexName);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(0);

		String username = sysLogPageQuery.getAccount();
		String requestType = sysLogPageQuery.getRequestType();
		String startDateStr = sysLogPageQuery.getStartDate();
		String endDateStr = sysLogPageQuery.getEndDate();

		String level = sysLogPageQuery.getLevel();
		String platform = sysLogPageQuery.getPlatform();

		// init ES query
		BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
		if (StringUtils.isNotBlank(username)) {
			booleanQueryBuilder.must(QueryBuilders.wildcardQuery("username", "*" + username + "*"));
		}

		if (StringUtils.isNotBlank(requestType)) {
			booleanQueryBuilder.must(QueryBuilders.matchQuery("requestType.keyword", requestType));
		}

		if (StringUtils.isNotBlank(level)) {
			booleanQueryBuilder.must(QueryBuilders.matchQuery("level.keyword", level));
		}

		if (StringUtils.isNotBlank(platform)) {
			booleanQueryBuilder.must(QueryBuilders.matchQuery("platform.keyword", platform));
		}

		if (StringUtils.isNotBlank(sysLogPageQuery.getStartDate())
				&& StringUtils.isBlank(sysLogPageQuery.getEndDate())) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = dateFormat.parse(sysLogPageQuery.getStartDate());
			booleanQueryBuilder.must(QueryBuilders.rangeQuery("@timestamp").from(startDate));
		}
		if (StringUtils.isBlank(sysLogPageQuery.getStartDate())
				&& StringUtils.isNotBlank(sysLogPageQuery.getEndDate())) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date endDate = dateFormat.parse(sysLogPageQuery.getEndDate());
			booleanQueryBuilder.must(QueryBuilders.rangeQuery("@timestamp").to(endDate));
		}
		if (StringUtils.isNotBlank(sysLogPageQuery.getStartDate())
				&& StringUtils.isNotBlank(sysLogPageQuery.getEndDate())) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = dateFormat.parse(sysLogPageQuery.getStartDate());
			Date endDate = dateFormat.parse(sysLogPageQuery.getEndDate());
			booleanQueryBuilder.must(QueryBuilders.rangeQuery("@timestamp").from(startDate).to(endDate));
		}

		DateHistogramAggregationBuilder aggregation = AggregationBuilders.dateHistogram("groupDate")
				.field("record_time").dateHistogramInterval(DateHistogramInterval.DAY).format("yyyy-MM-dd");

		TermsAggregationBuilder s1agg = AggregationBuilders.terms("action_a").field("platform.keyword");
		TermsAggregationBuilder s2agg = AggregationBuilders.terms("action_b").field("level.keyword");
		TermsAggregationBuilder s3agg = AggregationBuilders.terms("action_c").field("path.keyword");
		TermsAggregationBuilder s4agg = AggregationBuilders.terms("action_d").field("username.keyword");

		s1agg.size(50);
		s2agg.size(50);
		s3agg.size(1000);
		s4agg.size(5000);

		s3agg.subAggregation(s4agg);
		s2agg.subAggregation(s3agg);

		s1agg.subAggregation(s2agg);
		aggregation.subAggregation(s1agg);

		searchSourceBuilder.aggregation(aggregation);
		searchSourceBuilder.query(booleanQueryBuilder);
		searchRequest.source(searchSourceBuilder);
		SearchResponse s = client.search(searchRequest, RequestOptions.DEFAULT);

		return s;

	}

}
