package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(DataSourceClientProperties.class)
public class DataSourceClientConfiguration {

  @Valid
  private final DataSourceClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("data-source"))
  private Channel dataSourceChannel;

  @Bean
  DataSourceClient dataSourceClient() {
    return new DataSourceClient(
        CategoryServiceGrpc.newBlockingStub(dataSourceChannel), properties.getTimeout());
  }
}
