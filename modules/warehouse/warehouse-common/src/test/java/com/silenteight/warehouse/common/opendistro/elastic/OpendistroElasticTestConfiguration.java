package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.OpendistroModule;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    OpendistroModule.class
})
@ImportAutoConfiguration({
    ElasticsearchRestClientAutoConfiguration.class,
    JacksonAutoConfiguration.class,
})
@RequiredArgsConstructor
public class OpendistroElasticTestConfiguration {
}