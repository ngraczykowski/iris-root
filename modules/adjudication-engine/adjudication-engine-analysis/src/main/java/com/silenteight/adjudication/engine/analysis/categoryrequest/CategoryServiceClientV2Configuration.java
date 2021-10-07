package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CategoryServiceClientProperties.class)
@Profile("!datasourcev1")
public class CategoryServiceClientV2Configuration {

  @Valid
  private final CategoryServiceClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("datasource"))
  private Channel dataSourceChannel;

  @Bean
  CategoryServiceClient dataSourceClient() {
    var stub = CategoryValueServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();

    return new CategoryServiceClientV2(stub, properties.getTimeout());
  }
}
