package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc.CategoryServiceBlockingStub;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.category.v2.*;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
    CategoriesConfigurationProperties.class, CategoriesGrpcConfigurationProperties.class })
class CategoriesGrpcServiceConfiguration {

  @GrpcClient(KnownServices.CATEGORY)
  private CategoryServiceBlockingStub categoryServiceBlockingStub;

  @GrpcClient(KnownServices.CATEGORY_VALUE)
  private CategoryValueServiceBlockingStub categoryValueServiceBlockingStub;

  @Bean
  @Profile("!dev")
  CategoryServiceClient categoryServiceClientApi(
      CategoriesGrpcConfigurationProperties grpcProperties) {
    return new CategoryGrpcAdapter(
        categoryServiceBlockingStub, grpcProperties.getCategoryDeadline().getSeconds());
  }

  @Bean
  @Profile("dev")
  CategoryServiceClient categoryServiceClientMock() {
    return new CategoryServiceClientMock();
  }

  @Bean
  @Profile("!dev")
  CategoryValuesServiceClient categoryValuesServiceClientApi(
      CategoriesGrpcConfigurationProperties grpcProperties) {
    return new CategoryValuesGrpcAdapter(
        categoryValueServiceBlockingStub, grpcProperties.getCategoryValueDeadline().getSeconds());
  }

  @Bean
  @Profile("dev")
  CategoryValuesServiceClient categoryValuesServiceClientMock() {
    return new CategoryValuesServiceClientMock();
  }

  private static class CategoryServiceClientMock implements CategoryServiceClient {

    @Override
    public BatchCreateCategoriesOut createCategories(BatchCreateCategoriesIn request) {
      return null;
    }
  }

  private static class CategoryValuesServiceClientMock implements CategoryValuesServiceClient {

    @Override
    public BatchCreateCategoryValuesOut createCategoriesValues(
        BatchCreateCategoryValuesIn request) {
      return null;
    }
  }
}
