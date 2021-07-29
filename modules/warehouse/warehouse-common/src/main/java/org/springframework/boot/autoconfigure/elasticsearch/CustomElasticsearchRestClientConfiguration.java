package org.springframework.boot.autoconfigure.elasticsearch;

import org.springframework.context.annotation.Import;

@Import(ElasticsearchRestClientAutoConfiguration.RestClientBuilderConfiguration.class)
public class CustomElasticsearchRestClientConfiguration {
}
