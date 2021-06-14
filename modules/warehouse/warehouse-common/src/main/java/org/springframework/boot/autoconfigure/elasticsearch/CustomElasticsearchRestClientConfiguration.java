package org.springframework.boot.autoconfigure.elasticsearch;

import org.springframework.context.annotation.Import;

@Import(ElasticsearchRestClientConfigurations.RestClientBuilderConfiguration.class)
public class CustomElasticsearchRestClientConfiguration {
}
