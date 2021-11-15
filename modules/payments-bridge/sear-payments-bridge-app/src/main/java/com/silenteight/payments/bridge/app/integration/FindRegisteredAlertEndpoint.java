package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlertWithMatches;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertFircoId;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertIdSet;
import com.silenteight.payments.bridge.firco.alertmessage.port.FindAlertIdSetUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.RegisteredAlert;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@MessageEndpoint
@RequiredArgsConstructor
@Slf4j
class FindRegisteredAlertEndpoint {

  static final String FIND_REGISTERED_ALERT_CHANNEL = "FindRegisteredAlertEndpoint_channel";

  private final FindAlertIdSetUseCase findAlertIdSetUseCase;
  private final FindRegisteredUseCase findRegisteredUseCase;

  @ServiceActivator(inputChannel = FIND_REGISTERED_ALERT_CHANNEL)
  List<RegisteredAlert> apply(List<FindRegisteredAlertRequest> registeredAlert) {
    var alertIdSets = findAlertIdSetUseCase.find(mapToFircoIds(registeredAlert));
    if (alertIdSets.isEmpty()) {
      return List.of();
    }

    var alertIds = alertIdSets.stream()
        .map(AlertIdSet::getAlertId).collect(Collectors.toList());

    var alertsMap =  findRegisteredUseCase.find(alertIds)
        .stream().collect(Collectors.toMap(
            RegisteredAlertWithMatches::getAlertId,
            Function.identity()));

    return alertIdSets.stream()
        .map(a -> mapToAlert(a, alertsMap))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private List<AlertFircoId> mapToFircoIds(List<FindRegisteredAlertRequest> registeredAlert) {
    return registeredAlert.stream().map(r -> AlertFircoId.builder()
            .messageId(r.getMessageId())
            .systemId(r.getSystemId()).build())
        .collect(Collectors.toList());
  }

  private RegisteredAlert mapToAlert(AlertIdSet alertIdSet,
      Map<UUID, RegisteredAlertWithMatches> alertsMap) {
    var alert = alertsMap.get(alertIdSet.getAlertId());
    if (alert == null) {
      log.error("No registration details (alertName, matches) found for alertId: {}",
          alertIdSet.getAlertId());
      return null;
    }
    return new RegisteredAlert(alertIdSet.getAlertId(), alertIdSet.getSystemId(),
        alertIdSet.getMessageId(), alert.getAlertName(), alert.getMatches());
  }

  @Bean(FIND_REGISTERED_ALERT_CHANNEL)
  MessageChannel messageChannel() {
    return new DirectChannel();
  }
}
