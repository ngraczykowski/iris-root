package com.silenteight.warehouse.configurationmanagement.loader;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.configurationmanagement.ConfigurationManagementModule;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    ConfigurationManagementModule.class,
    ElasticsearchRestClientModule.class,
    OpendistroModule.class,
    TokenModule.class
})
@ImportAutoConfiguration({
    JacksonAutoConfiguration.class,
})
@RequiredArgsConstructor
class LoadOpendistroConfigurationTestConfiguration {
}
