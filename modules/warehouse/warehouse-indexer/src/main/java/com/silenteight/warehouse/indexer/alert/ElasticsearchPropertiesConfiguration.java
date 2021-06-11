package com.silenteight.warehouse.indexer.alert;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
class ElasticsearchPropertiesConfiguration {
}
