package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.AlertState;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.silenteight.fab.dataprep.domain.RegistrationConverter.convert;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
abstract class BaseUseCase {

  protected final AlertService alertService;
  protected final RegistrationService registrationService;

  protected List<RegisteredAlert> registerNewAlerts(
      Map<String, ParsedAlertMessage> extractedAlerts) {
    List<RegisteredAlert> registeredNotInUdsAlerts = new LinkedList<>();
    Map<String, ParsedAlertMessage> alertsToRegister = new HashMap<>();

    extractedAlerts.forEach((messageName, parsedAlertMessage) -> alertService
        .getAlertItem(parsedAlertMessage.getDiscriminator())
        .ifPresentOrElse(
            alertItem -> {
              if (alertItem.getState() == AlertState.REGISTERED) {
                registeredNotInUdsAlerts.add(convert(alertItem, parsedAlertMessage));
              }
            },
            () -> alertsToRegister.put(messageName, parsedAlertMessage)));

    if (!alertsToRegister.isEmpty()) {
      log.debug("Registering data, count: {}", alertsToRegister.size());
      registrationService.registerAlertsAndMatches(alertsToRegister)
          .forEach(registeredAlert -> {
            saveAlertItem(registeredAlert);
            registeredNotInUdsAlerts.add(registeredAlert);
          });
    }

    return registeredNotInUdsAlerts;
  }

  private void saveAlertItem(RegisteredAlert registeredAlert) {
    List<String> matchNames = registeredAlert.getMatches()
        .stream()
        .map(Match::getMatchName)
        .collect(toList());
    var discriminator = registeredAlert.getDiscriminator();
    alertService.save(discriminator, registeredAlert.getAlertName(), matchNames);
  }
}
