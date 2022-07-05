package com.silenteight.bridge.core.registration.adapter.outgoing.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.library.v1.alert.AlertServiceClient;
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister;
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRegistrationService;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertRegistrationGrpcAdapter implements AlertRegistrationService {

  private final AlertRegistrationMapper mapper;
  private final AlertServiceClient alertServiceClient;

  @Override
  @Retryable(value = AdjudicationEngineLibraryRuntimeException.class,
      maxAttemptsExpression = "${grpc.client.retry.max-attempts}",
      backoff = @Backoff(
          multiplierExpression = "${grpc.client.retry.multiplier}",
          delayExpression = "${grpc.client.retry.delay-in-milliseconds}"))
  public RegisteredAlerts registerAlerts(AlertsToRegister alertsToRegister) {
    var request = mapper.toRequest(alertsToRegister);
    var result = alertServiceClient.registerAlertsAndMatches(request);

    return mapper.toRegisteredAlerts(result);
  }
}
