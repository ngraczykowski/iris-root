package com.silenteight.warehouse.indexer.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.common.time.TimeModule;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    AlertConfiguration.class,
    TestElasticSearchModule.class,
    TimeModule.class
})
@ImportAutoConfiguration({
    ElasticsearchRestClientAutoConfiguration.class
})
@RequiredArgsConstructor
class AlertTestConfiguration {
}
