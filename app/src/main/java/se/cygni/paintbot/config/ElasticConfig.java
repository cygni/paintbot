package se.cygni.paintbot.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"production"})
@Configuration
public class ElasticConfig {

    private static final Logger log = LoggerFactory.getLogger(ElasticConfig.class);

    @Value("${paintbot.elastic.host}")
    private String elasticHost;

    @Value("${paintbot.elastic.port}")
    private int elasticPort;

    @Bean
    public RestHighLevelClient elasticSearchClient() throws Exception {
        log.info("Connecting to ElasticSearch on: " + elasticHost + ":" + elasticPort);
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elasticHost, elasticPort, "https")));
    }
}