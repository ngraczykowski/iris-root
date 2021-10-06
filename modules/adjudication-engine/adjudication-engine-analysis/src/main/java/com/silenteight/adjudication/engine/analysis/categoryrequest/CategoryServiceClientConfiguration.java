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
@EnableConfigurationProperties(CategoryServiceClientProperties.class)
public class CategoryServiceClientConfiguration {

  @Valid
  private final CategoryServiceClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("datasource"))
  private Channel dataSourceChannel;

  @Bean
  CategoryServiceClient dataSourceClient() {
    var stub = CategoryServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();

    return new CategoryServiceClient(stub, properties.getTimeout());
  }
}
