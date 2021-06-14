package com.silenteight.warehouse.common.elastic;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.TokenModule;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    ElasticsearchRestClientModule.class,
    TokenModule.class
})
@RequiredArgsConstructor
public class RestHighLevelElasticClientTestConfiguration {
}
