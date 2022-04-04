package com.silenteight.connector.ftcc.ingest.adapter.incoming.grpc;

import lombok.NonNull;

import com.silenteight.connector.ftcc.request.get.MessageByIdsQuery;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcIncomingConfiguration {

  @GrpcService
  @Bean
  AlertMessageDetailsGrpcService alertMessageDetailsGrpcService(
      @NonNull MessageByIdsQuery messageByIdsQuery) {

    return new AlertMessageDetailsGrpcService(messageByIdsQuery);
  }
}
