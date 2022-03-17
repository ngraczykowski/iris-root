package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.fab.dataprep.infrastructure.grpc.RegistrationGrpcServiceConfiguration.GrpcProperties;
import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub;
import com.silenteight.registration.api.library.v1.*;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import javax.validation.constraints.NotNull;

import static java.util.stream.Collectors.toList;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GrpcProperties.class)
class RegistrationGrpcServiceConfiguration {

  @GrpcClient(KnownServices.REGISTRATION)
  private RegistrationServiceBlockingStub registrationServiceBlockingStub;

  @Bean
  @Profile("!dev")
  RegistrationServiceClient registrationServiceClientGrpcApi(GrpcProperties grpcProperties) {
    return new RegistrationServiceGrpcAdapter(
        registrationServiceBlockingStub, grpcProperties.getDeadline().getSeconds());
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
          .alertName(alertWithMatchesIn.getAlertId().replace("messages", "alerts"))
          .alertStatus(AlertStatusOut.SUCCESS)
          .registeredMatches(alertWithMatchesIn.getMatches().stream().map(
              RegistrationServiceClientMock::convert).collect(toList()))
          .build();
    }

    private static RegisteredMatchOut convert(MatchIn matchIn) {
      return RegisteredMatchOut
          .builder()
          .matchId(matchIn.getMatchId())
          .matchName(matchIn.getMatchId().replace("hits", "matches"))
          .build();
    }
  }

  @Validated
  @ConstructorBinding
  @ConfigurationProperties("grpc.client.registration")
  @Value
  static class GrpcProperties {

    @NotNull
    Duration deadline;
  }
}
