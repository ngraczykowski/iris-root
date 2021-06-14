package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    OpendistroModule.class,
    ElasticsearchRestClientModule.class
})
@ImportAutoConfiguration({
    JacksonAutoConfiguration.class,
})
@RequiredArgsConstructor
public class OpendistroElasticTestConfiguration {

  @Bean
  UserAwareTokenProvider userAwareTokenProvider() {
    return mock(UserAwareTokenProvider.class);
  }
}
