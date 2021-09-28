package com.silenteight.warehouse.indexer.query.streaming;

import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.environment.EnvironmentModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    ScrollSearchConfiguration.class,
    ElasticsearchRestClientModule.class,
    EnvironmentModule.class,
    TestElasticSearchModule.class,
    TokenModule.class
})
class ScrollSearchTestConfiguration {
}
