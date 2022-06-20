/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.infrastructure.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc.CategoryServiceBlockingStub;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;
import com.silenteight.iris.bridge.scb.feeding.infrastructure.util.KnownServices;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceAdapter;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryGrpcAdapter;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryServiceClient;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryValuesGrpcAdapter;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryValuesServiceClient;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(UniversalDataSourceGrpcConfigurationProperties.class)
class UniversalDataSourceGrpcServiceConfiguration {

  @GrpcClient(KnownServices.UNIVERSAL_DATA_SOURCE)
  CategoryServiceBlockingStub categoryServiceBlockingStub;

  @GrpcClient(KnownServices.UNIVERSAL_DATA_SOURCE)
  CategoryValueServiceBlockingStub categoryValueServiceBlockingStub;

  @GrpcClient(KnownServices.UNIVERSAL_DATA_SOURCE)
  AgentInputServiceBlockingStub agentInputServiceBlockingStub;

  @Bean
  @Profile("!dev")
  CategoryServiceClient categoryServiceClient(
      UniversalDataSourceGrpcConfigurationProperties grpcProperties) {
    return new CategoryGrpcAdapter(
        categoryServiceBlockingStub, grpcProperties.udsDeadline().getSeconds());
  }

  @Bean
  @Profile("!dev")
  CategoryValuesServiceClient categoryValuesServiceClient(
      UniversalDataSourceGrpcConfigurationProperties grpcProperties) {
    return new CategoryValuesGrpcAdapter(
        categoryValueServiceBlockingStub, grpcProperties.udsDeadline().getSeconds());
  }

  @Bean
  @Profile("!dev")
  AgentInputServiceClient agentInputServiceClient(
      UniversalDataSourceGrpcConfigurationProperties grpcProperties) {
    return new AgentInputServiceAdapter(
        agentInputServiceBlockingStub, grpcProperties.udsDeadline().getSeconds());
  }

  @Bean
  @Profile("dev")
  CategoryServiceClient categoryServiceClientMock() {
    return new CategoryServiceClientMock();
  }

  @Bean
  @Profile("dev")
  CategoryValuesServiceClient categoryValuesServiceClientMock() {
    return new CategoryValuesServiceClientMock();
  }

  @Bean
  @Profile("dev")
  AgentInputServiceClient agentInputServiceClientMock() {
    return new AgentInputServiceClientMock();
  }
}
