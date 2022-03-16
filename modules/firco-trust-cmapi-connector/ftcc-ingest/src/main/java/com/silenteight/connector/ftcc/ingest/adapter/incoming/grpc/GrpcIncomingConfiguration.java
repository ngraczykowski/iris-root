package com.silenteight.connector.ftcc.ingest.adapter.incoming.grpc;

import com.silenteight.connector.ftcc.ingest.domain.MessageDetailsQuery;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcIncomingConfiguration {

  @GrpcService
  @Bean
  AlertMessageDetailsGrpcService alertMessageDetailsGrpcService(
      MessageDetailsQuery messageDetailsQuery) {

    return new AlertMessageDetailsGrpcService(messageDetailsQuery);
  }
}
