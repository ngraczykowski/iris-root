package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    ElasticsearchRestClientModule.class,
    OpendistroModule.class,
    TestElasticSearchModule.class,
    TokenModule.class
})
@ImportAutoConfiguration({
    JacksonAutoConfiguration.class,
})
@RequiredArgsConstructor
public class OpendistroKibanaTestConfiguration {
}
