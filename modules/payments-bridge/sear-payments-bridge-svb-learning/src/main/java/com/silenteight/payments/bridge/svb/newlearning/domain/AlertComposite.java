package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.svb.learning.reader.domain.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.RegisteredAlert;

import java.util.List;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class AlertComposite {

  AlertDetails alertDetails;

  List<HitComposite> hits;

  List<ActionComposite> actions;

  public FindRegisteredAlertRequest toFindRegisterAlertRequest() {
    return alertDetails.toFindRegisterAlertRequest();
  }

  public RegisterAlertRequest toRegisterAlertRequest(Long jobId) {
    return alertDetails.toRegisterAlertRequest(jobId, hits);
  }

  public LearningRegisteredAlert toLearningRegisteredAlert(RegisteredAlert registeredAlert) {
    var registeredMatches = getRegisteredMatches(registeredAlert);
    return LearningRegisteredAlert
        .builder()
        .alertRegistrationSource(AlertRegistrationSource.FIRCO)
        .alertName(registeredAlert.getAlertName())
        .alertDetails(alertDetails)
        .registeredMatches(registeredMatches)
        .build();
  }

  public LearningRegisteredAlert toLearningRegisteredAlert(RegisterAlertResponse registeredAlert) {
    var registeredMatches =
        registeredAlert
            .getMatchResponses()
            .stream()
            .map(this::getRegisteredMatch)
            .collect(toList());
    return LearningRegisteredAlert
        .builder()
        .alertRegistrationSource(AlertRegistrationSource.LEARNING)
        .alertName(registeredAlert.getAlertName())
        .alertDetails(alertDetails)
        .registeredMatches(registeredMatches)
        .build();
  }

  @Nonnull
  private List<RegisteredMatch> getRegisteredMatches(RegisteredAlert registeredAlert) {
    var matches = registeredAlert.getMatches();
    return hits
        .stream()
        .map(hit -> {
          var match =
              matches.stream().filter(m -> hit.getMatchId().equals(m.getMatchId())).findFirst();
          match.orElseThrow();
          return RegisteredMatch
              .builder()
              .matchName(match.get().getMatchName())
              .hitComposite(hit)
              .build();
        }).collect(toList());
  }


  private RegisteredMatch getRegisteredMatch(RegisterMatchResponse match) {
    var hit = hits.stream().filter(h -> h.getMatchId().equals(match.getMatchId())).findFirst();
    hit.orElseThrow();
    return RegisteredMatch
        .builder()
        .hitComposite(hit.get())
        .matchName(match.getMatchName())
        .build();
  }
}
