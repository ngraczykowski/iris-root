package com.silenteight.simulator.dataset.domain;

import com.silenteight.simulator.dataset.DatasetModule;

import io.grpc.Channel;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = DatasetModule.class)
class DatasetTestConfiguration {

  @Bean("adjudication-engine")
  Channel adjudicationEngineChannel() {
    return Mockito.mock(Channel.class);
  }
}
