package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub;
import com.silenteight.registration.api.library.v1.*;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static java.util.stream.Collectors.toList;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RegistrationGrpcConfigurationProperties.class)
class RegistrationGrpcServiceConfiguration {

  @GrpcClient(KnownServices.REGISTRATION)
  private RegistrationServiceBlockingStub registrationServiceBlockingStub;

  @Bean
  @Profile("!dev")
  RegistrationServiceClient registrationServiceClientGrpcApi(
      RegistrationGrpcConfigurationProperties grpcProperties) {
    return new RegistrationServiceGrpcAdapter(
        registrationServiceBlockingStub, grpcProperties.getRegistrationDeadline().getSeconds());
  }

  @Bean
  @Profile("dev")
  RegistrationServiceClient registrationServiceClientMock() {
    return new RegistrationServiceClientMock();
  }

  private static class RegistrationServiceClientMock implements RegistrationServiceClient {

    @Override
    public EmptyOut registerBatch(RegisterBatchIn request) {
      return EmptyOut.getInstance();
    }

    @Override
    public EmptyOut notifyBatchError(NotifyBatchErrorIn request) {
      return EmptyOut.getInstance();
    }

    @Override
    public RegisterAlertsAndMatchesOut registerAlertsAndMatches(
        RegisterAlertsAndMatchesIn request) {
      return RegisterAlertsAndMatchesOut
          .builder()
          .registeredAlertWithMatches(request.getAlertsWithMatches().stream().map(
              RegistrationServiceClientMock::convert).collect(toList()))
          .build();
    }

    private static RegisteredAlertWithMatchesOut convert(AlertWithMatchesIn alertWithMatchesIn) {
      return RegisteredAlertWithMatchesOut.builder()
          .alertId(alertWithMatchesIn.getAlertId())
          .alertName("exampleAlert")
          .alertStatus(AlertStatusOut.SUCCESS)
          .registeredMatches(alertWithMatchesIn.getMatches().stream().map(
              RegistrationServiceClientMock::convert).collect(toList()))
          .build();
    }

    private static RegisteredMatchOut convert(MatchIn matchIn) {
      return RegisteredMatchOut
          .builder()
          .matchId(matchIn.getMatchId())
          .matchName("exampleMatch")
          .build();
    }
  }
}
