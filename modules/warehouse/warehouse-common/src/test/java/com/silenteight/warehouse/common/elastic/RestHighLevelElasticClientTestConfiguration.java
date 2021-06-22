package com.silenteight.warehouse.common.elastic;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    ElasticsearchRestClientModule.class,
    TestElasticSearchModule.class,
    TokenModule.class
})
@RequiredArgsConstructor
public class RestHighLevelElasticClientTestConfiguration {
}
