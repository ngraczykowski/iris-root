package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.AlertState;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match;
import com.silenteight.fab.dataprep.domain.model.SolvingRequest;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
class SolvingUseCase {

  private final AlertService alertService;
  private final FeedingFacade feedingFacade;
  private final RegistrationService registrationService;

  void handle(SolvingRequest solvingRequest) {
    registerAlert(solvingRequest.getExtractedAlerts())
        .forEach(registeredAlert -> {
          feedingFacade.etlAndFeedUds(registeredAlert);
          var discriminator = registeredAlert.getDiscriminator();
          alertService.setAlertState(discriminator, AlertState.IN_UDS);
        });
  }

  private List<RegisteredAlert> registerAlert(Map<String, ParsedAlertMessage> extractedAlerts) {
    var registeredAlerts = registrationService.registerAlertsAndMatches(extractedAlerts);
    registeredAlerts.forEach(this::saveAlertItem);
    return registeredAlerts;
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
