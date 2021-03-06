package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.AlertErrorDescription;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.registration.api.library.v1.RegisterAlertsAndMatchesIn;
import com.silenteight.registration.api.library.v1.RegisterAlertsAndMatchesOut;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.silenteight.fab.dataprep.domain.RegistrationConverter.convert;
import static com.silenteight.fab.dataprep.domain.RegistrationConverter.createFailedAlertWithMatchesIn;
import static com.silenteight.fab.dataprep.domain.RegistrationConverter.createFailedRegisteredAlert;
import static com.silenteight.fab.dataprep.domain.RegistrationConverter.getBatchName;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegistrationService {

  private final RegistrationServiceClient registrationServiceClient;

  public List<RegisteredAlert> registerAlertsAndMatches(
      Map<String, ParsedAlertMessage> parsedAlertMessagesMap) {
    Collection<ParsedAlertMessage> parsedAlertMessages = parsedAlertMessagesMap.values();
    RegisterAlertsAndMatchesIn registerAlertsAndMatchesIn = RegisterAlertsAndMatchesIn
        .builder()
        .batchId(getBatchName(parsedAlertMessages))
        .alertsWithMatches(
            parsedAlertMessages
                .stream()
                .map(RegistrationConverter::convert)
                .collect(toList()))
        .build();
    log.debug("Try to register Alerts with Matches: {}", registerAlertsAndMatchesIn);
    RegisterAlertsAndMatchesOut result =
        registrationServiceClient.registerAlertsAndMatches(registerAlertsAndMatchesIn);

    return result
        .getRegisteredAlertWithMatches()
        .stream()
        .map(registeredAlertWithMatchesOut -> convert(
            registeredAlertWithMatchesOut,
            parsedAlertMessagesMap.get(registeredAlertWithMatchesOut.getAlertId())))
        .collect(toList());
  }

  public List<RegisteredAlert> registerFailedAlerts(
      List<String> messageNames,
      String batchName,
      AlertErrorDescription errorDescription) {
    RegisterAlertsAndMatchesIn registerAlertsAndMatchesIn = RegisterAlertsAndMatchesIn
        .builder()
        .batchId(batchName)
        .alertsWithMatches(
            messageNames
                .stream()
                .map(messageName -> createFailedAlertWithMatchesIn(messageName, errorDescription))
                .collect(toList()))
        .build();
    RegisterAlertsAndMatchesOut result =
        registrationServiceClient.registerAlertsAndMatches(registerAlertsAndMatchesIn);

    return result
        .getRegisteredAlertWithMatches()
        .stream()
        .map(registeredAlertWithMatchesOut -> createFailedRegisteredAlert(
            registeredAlertWithMatchesOut, batchName, errorDescription))
        .collect(toList());
  }
}
