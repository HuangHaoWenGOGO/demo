package seassoon.rule.es;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElasticUtils {

	@Autowired
	private RestHighLevelClient client;


	/**
	 * 查询
	 *
	 * @param index
	 * @param keyword
	 * @param searchField
	 * @return
	 */
	public SearchResponse keywordquery(String index, String keyword, String searchField,Integer page,Integer pageSize) throws IOException{
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(pageSize).from(page*pageSize);
		
		MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(searchField, keyword);
		
		searchSourceBuilder.query(matchQueryBuilder);
		searchRequest.source(searchSourceBuilder);
		return client.search(searchRequest, RequestOptions.DEFAULT);
	}
	/**
	 * 查询
	 *
	 * @param index
	 * @param keyword
	 * @param searchField
	 * @return
	 */
	public SearchResponse query(String index, String keyword, String searchField,Integer page,Integer pageSize) throws IOException{
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(pageSize).from(page*pageSize);
		
		MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(searchField, keyword).analyzer("ik_smart");
		
		searchSourceBuilder.query(matchQueryBuilder);
		searchRequest.source(searchSourceBuilder);
		return client.search(searchRequest, RequestOptions.DEFAULT);
	}
	/**
	 * 查询
	 *
	 * @param index
	 * @param keyword
	 * @param searchField
	 * @return
	 */
	public SearchResponse query(String index,Integer page,Integer pageSize) throws IOException{
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
				.size(pageSize)
				.from(page*pageSize)
				.sort("createTime", SortOrder.DESC);
		
		
		searchRequest.source(searchSourceBuilder);
		return client.search(searchRequest, RequestOptions.DEFAULT);
	}
	
	
	/**
	 * term 级别的查询
	 */
	public SearchResponse termQuery(String index, String keyword, String searchField,Integer size) throws IOException{
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(size);
	
		searchSourceBuilder.query(QueryBuilders.termsQuery(searchField,keyword));
		searchRequest.source(searchSourceBuilder);
		
		return client.search(searchRequest, RequestOptions.DEFAULT);
	}
	/**
	 * term 级别的查询- 分页
	 */
	public SearchResponse termQueryPage(String index, String keyword, String searchField,Integer page,Integer pageSize) throws IOException{
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(pageSize).from(page*pageSize);
	
		searchSourceBuilder.query(QueryBuilders.termsQuery(searchField,keyword));
		searchRequest.source(searchSourceBuilder);
		
		return client.search(searchRequest, RequestOptions.DEFAULT);
	}
	
	/**
	 * term 批量查询
	 */
	public SearchResponse termBatchQuery(String index, List<String> keyword, String searchField,Integer size) throws IOException{
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(size);
	
		searchSourceBuilder.query(QueryBuilders.termsQuery(searchField,keyword));
		
		searchRequest.source(searchSourceBuilder);
		
		return client.search(searchRequest, RequestOptions.DEFAULT);
	}
	
	/**
	 * term 批量查询 ——根据企业名聚合
	 */
	public SearchResponse termBatchQueryAggByName(String index, List<String> keyword, String searchField,Integer size) throws IOException{
		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(size);
	
		searchSourceBuilder.query(QueryBuilders.termsQuery(searchField,keyword));
		
		TermsAggregationBuilder aggregation  = AggregationBuilders
				.terms("corporate_name")
				.field("corporate_name.keyword")
				.executionHint("map")
				.size(keyword.size());
		searchSourceBuilder.aggregation(aggregation);
		
		searchRequest.source(searchSourceBuilder);
		
		return client.search(searchRequest, RequestOptions.DEFAULT);
	}
	
	
	
	
	/**
	 * 模糊查询
	 *
	 * @param index
	 * @param keyword
	 * @param searchField
	 * @return
	 */
	public SearchResponse fuzzyQuey(String index, String keyword, String searchField) throws IOException {

		// fixme 加一些模糊搜索的条件限制

		SearchRequest searchRequest = new SearchRequest(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(20);
		FuzzyQueryBuilder fuzzyQueryBuilder = new FuzzyQueryBuilder(searchField, keyword);
		searchSourceBuilder.query(fuzzyQueryBuilder);
		searchRequest.source(searchSourceBuilder);
		return client.search(searchRequest, RequestOptions.DEFAULT);

	}

	public void batchSave(String index, String type, Map<String, String> jsonMap) {

		BulkRequest bulkRequest = new BulkRequest();
		for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
			bulkRequest.add(new IndexRequest(index, type, entry.getKey()).source(entry.getValue(), Requests.INDEX_CONTENT_TYPE));
		}
		try {
			BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void batchSave2(String index, String type, Map<String, Map<String, Object>> valueMap) {

		BulkRequest bulkRequest = new BulkRequest();
		for (Map.Entry<String, Map<String, Object>> entry : valueMap.entrySet()) {
			bulkRequest.add(new IndexRequest(index, type, entry.getKey()).source(JSONObject.toJSONString(entry.getValue()), Requests.INDEX_CONTENT_TYPE));
		}
		try {
			BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
