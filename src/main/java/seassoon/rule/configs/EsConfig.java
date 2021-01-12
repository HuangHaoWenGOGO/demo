package seassoon.rule.configs;

import java.util.ArrayList;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {
	@Value("${elasticsearch.ip}")
	private  String hosts ; // 集群地址，多个用,隔开
	
	@Value("${elasticsearch.port}")
	private  int port; // 使用的端口号
	
	@Value("${elasticsearch.user}")
	private String user;

	@Value("${elasticsearch.password}")
	private String password;
	
	private  String schema = "http"; // 使用的协议
	
	private  ArrayList<HttpHost> hostList = null;



	@Bean
	public RestHighLevelClient client() {

		
		hostList = new ArrayList<>();
		String[] hostStrs = hosts.split(",");
		for (String host : hostStrs) {
			hostList.add(new HttpHost(host, port, schema));
		}
		
		final CredentialsProvider credentialsProvider =
				new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(user, password));

		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(hostList.toArray(new HttpHost[0]))
				.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
						.setDefaultCredentialsProvider(credentialsProvider)
				).setMaxRetryTimeoutMillis(60000)
			);
		

		return client;
	}
	

}
