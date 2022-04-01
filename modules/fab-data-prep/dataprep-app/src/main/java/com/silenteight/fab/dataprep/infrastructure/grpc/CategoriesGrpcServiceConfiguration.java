package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc.CategoryServiceBlockingStub;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesGrpcServiceConfiguration.GrpcCategoryProperties;
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesGrpcServiceConfiguration.GrpcCategoryValueProperties;
import com.silenteight.universaldatasource.api.library.category.v2.*;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
    CategoriesConfigurationProperties.class, GrpcCategoryProperties.class,
    GrpcCategoryValueProperties.class })
public class CategoriesGrpcServiceConfiguration {

  @GrpcClient(KnownServices.CATEGORY)
  private CategoryServiceBlockingStub categoryServiceBlockingStub;

  @GrpcClient(KnownServices.CATEGORY_VALUE)
  private CategoryValueServiceBlockingStub categoryValueServiceBlockingStub;

  @Bean
  @Profile("!dev")
  CategoryServiceClient categoryServiceClientApi(
      @Valid GrpcCategoryProperties grpcCategoryProperties) {
    return new CategoryGrpcAdapter(
        categoryServiceBlockingStub, grpcCategoryProperties.getDeadline().getSeconds());
  }

  @Bean
  @Profile("dev")
  CategoryServiceClient categoryServiceClientMock() {
    return new CategoryServiceClientMock();
  }

  @Bean
  @Profile("!dev")
  CategoryValuesServiceClient categoryValuesServiceClientApi(
      @Valid GrpcCategoryValueProperties grpcCategoryValueProperties) {
    return new CategoryValuesGrpcAdapter(
        categoryValueServiceBlockingStub, grpcCategoryValueProperties.getDeadline().getSeconds());
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

  @Validated
  @ConstructorBinding
  @ConfigurationProperties("grpc.client.category")
  @Value
  static class GrpcCategoryProperties {

    @NotNull
    Duration deadline;
  }

  @Validated
  @ConstructorBinding
  @ConfigurationProperties("grpc.client.category-value")
  @Value
  static class GrpcCategoryValueProperties {

    @NotNull
    Duration deadline;
  }
}
