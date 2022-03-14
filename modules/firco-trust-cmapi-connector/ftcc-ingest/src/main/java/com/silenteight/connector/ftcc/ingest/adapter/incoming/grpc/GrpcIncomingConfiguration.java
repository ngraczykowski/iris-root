package com.silenteight.connector.ftcc.ingest.adapter.incoming.grpc;

import com.silenteight.connector.ftcc.ingest.domain.MessageDetailsQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcIncomingConfiguration {

  @Bean
  AlertMessageDetailsGrpcService alertMessageDetailsGrpcService(
      MessageDetailsQuery messageDetailsQuery) {

    return new AlertMessageDetailsGrpcService(messageDetailsQuery);
  }
}
