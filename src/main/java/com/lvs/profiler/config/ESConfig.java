package com.lvs.profiler.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.camel.CamelContext;
import org.apache.camel.component.elasticsearch.ElasticsearchComponent;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.lvs.profiler.repository")
public class ESConfig{
    
	@Value("${elasticsearch.host}")
    public String host;
	
	@Value("${elasticsearch.clustername}")
	public String clusterName;
    
    @Value("${elasticsearch.port:9300}") 
    public int port;

    @Value("${elasticsearch.port2:9200}")
    public int port2;
    
	@Bean
	@SuppressWarnings("resource")
    public Client client(){
        TransportClient client = null;
        try{
        	Settings elasticsearchSettings = Settings.builder()
        	          .put("cluster.name", clusterName).build();
        	
            client = new PreBuiltTransportClient(elasticsearchSettings)
            .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
	
	@Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }

    @Bean
    @ConditionalOnClass(CamelContext.class)
    @ConditionalOnMissingBean(ElasticsearchComponent.class)
    public ElasticsearchComponent configureElasticsearchComponent(CamelContext camelContext) throws Exception {
        ElasticsearchComponent elasticsearchComponent = new ElasticsearchComponent();
        elasticsearchComponent.setHostAddresses(host + ":" + port2);
        camelContext.addComponent("elasticsearch-rest", elasticsearchComponent);
        return elasticsearchComponent;
    }

}