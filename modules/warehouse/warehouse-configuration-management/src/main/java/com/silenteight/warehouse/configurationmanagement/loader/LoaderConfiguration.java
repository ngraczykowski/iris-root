package com.silenteight.warehouse.configurationmanagement.loader;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroReportLoader;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroSavedObjectsLoader;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoaderConfiguration {

  @Bean
  LoadOpendistroConfigurationUseCase loadOpendistroConfigurationUseCase(
      OpendistroElasticClient opendistroElasticClient,
      OpendistroSavedObjectsLoader opendistroSavedObjectsLoader,
      OpendistroReportLoader opendistroReportLoader) {

    return new LoadOpendistroConfigurationUseCase(
        opendistroElasticClient,
        opendistroSavedObjectsLoader,
        opendistroReportLoader);
  }

  @Bean
  @GrpcService
  LoadConfigurationGrpcService loadConfigurationGrpcService(
      LoadOpendistroConfigurationUseCase loadOpendistroConfigurationUseCase) {

    return new LoadConfigurationGrpcService(loadOpendistroConfigurationUseCase);
  }
}
