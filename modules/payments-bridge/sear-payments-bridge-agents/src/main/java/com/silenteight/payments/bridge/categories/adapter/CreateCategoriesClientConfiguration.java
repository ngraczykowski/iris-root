package com.silenteight.payments.bridge.categories.adapter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc;
import com.silenteight.payments.bridge.categories.port.outgoing.CreateCategoriesClient;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CreateCategoriesClientProperties.class)
class CreateCategoriesClientConfiguration {

  @Valid
  private final CreateCategoriesClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("datasource"))
  private Channel categoriesDataChannel;

  @Bean
  CreateCategoriesClient createCategoriesClient() {
    var stub = CategoryServiceGrpc
        .newBlockingStub(categoriesDataChannel)
        .withWaitForReady();

    return new CreateCategoriesClientAdapter(stub, properties.getTimeout());
  }
}
