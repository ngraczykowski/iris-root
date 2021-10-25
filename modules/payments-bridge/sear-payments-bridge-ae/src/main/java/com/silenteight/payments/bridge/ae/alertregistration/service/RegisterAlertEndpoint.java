package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.Label;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.event.AlertInitializedEvent;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_INITIALIZED;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED;
import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.fromOffsetDateTime;

@MessageEndpoint
@Slf4j
@RequiredArgsConstructor
class RegisterAlertEndpoint {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;

  @ServiceActivator(inputChannel = ALERT_INITIALIZED, outputChannel = ALERT_REGISTERED)
  AlertRegisteredEvent apply(AlertInitializedEvent alertInitializedEvent) {
    AlertData alertData = alertInitializedEvent.getData(AlertData.class);
    AlertMessageDto alertDto = alertInitializedEvent.getData(AlertMessageDto.class);

    var matchIds = getMatchIds(alertDto);
    var request = RegisterAlertRequest.builder()
        .alertId(alertDto.getSystemID())
        .alertTime(fromOffsetDateTime(alertDto.getFilteredAt(ZoneOffset.UTC)))
        .priority(alertData.getPriority())
        .matchIds(matchIds)
        .label(Label.of("source", "CMAPI"))
        .label(Label.of("alertMessageId", alertData.getAlertId().toString()))
        .build();

    var alert = registerAlertUseCase.register(request);

    registeredAlertDataAccessPort.save(alertData.getAlertId(), alert.getAlertName());

    return new AlertRegisteredEvent(
        alertData.getAlertId(), alert.getAlertName(), alert.getMatchResponsesAsMap());
  }

  @Nonnull
  private static List<String> getMatchIds(AlertMessageDto alertDto) {
    var hits = alertDto.getHits();

    return IntStream.range(0, hits.size())
        .<Optional<String>>mapToObj(idx -> {
          var hit = hits.get(idx).getHit();
          return hit.isBlocking() ? Optional.of(hit.getMatchId(idx)) : Optional.empty();
        })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }
}
