package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    OpendistroModule.class,
    TestElasticSearchModule.class
})
@ImportAutoConfiguration({
    ElasticsearchRestClientAutoConfiguration.class,
    JacksonAutoConfiguration.class,
})
@RequiredArgsConstructor
public class OpendistroKibanaTestConfiguration {
}
