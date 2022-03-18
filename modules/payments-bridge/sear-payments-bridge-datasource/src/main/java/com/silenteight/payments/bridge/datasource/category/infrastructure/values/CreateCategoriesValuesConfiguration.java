package com.silenteight.payments.bridge.datasource.category.infrastructure.values;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CreateCategoriesValuesProperties.class)
class CreateCategoriesValuesConfiguration {

  @Valid
  private final CreateCategoriesValuesProperties properties;

  @Setter(onMethod_ = @GrpcClient("datasource"))
  private Channel categoriesDataChannel;

  @Bean
  DatasourceCategoryValueClient datasourceCategoryValueClient() {
    var stub = CategoryValueServiceGrpc
        .newBlockingStub(categoriesDataChannel)
        .withWaitForReady();

    return new DatasourceCategoryValueClient(stub, properties.getTimeout());
  }
}
