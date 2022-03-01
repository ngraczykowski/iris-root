package com.silenteight.payments.bridge.svb.learning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.svb.learning.domain.AlertComposite;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlertIdSet;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlertRequest;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexMatch;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class ProcessRegisteredService {

  private final IndexLearningUseCase indexLearningUseCase;
  private final IndexAnalystDecisionHelper indexAnalystDecisionHelper;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  void process(
      AlertComposite alertComposite, List<RegisteredAlert> registeredAlert) {
    indexLearningUseCase.index(
        createIndexRegisteredAlerts(alertComposite, registeredAlert));
  }

  private List<IndexAlertRequest> createIndexRegisteredAlerts(
      AlertComposite alertComposite, List<RegisteredAlert> registeredAlerts) {
    return registeredAlerts
        .stream()
        .map(ra -> createIndexRegisteredAlert(alertComposite, ra))
        .collect(toList());
  }

  private IndexAlertRequest createIndexRegisteredAlert(
      AlertComposite alertComposite, RegisteredAlert registeredAlert) {
    return new IndexAlertRequest(
        new IndexAlertIdSet(
            String.valueOf(alertComposite.getAlertDetails().getAlertId()),
            registeredAlert.getAlertName(),
            alertComposite.getSystemId(),
            alertComposite.getAlertDetails().getMessageId()),
        createIndexMatches(alertComposite, registeredAlert),
        indexAnalystDecisionHelper.getDecision(alertComposite.getActions()));
  }

  @Nonnull
  private static List<IndexMatch> createIndexMatches(
      AlertComposite alertComposite, RegisteredAlert registeredAlert) {
    return registeredAlert.getMatches()
        .stream()
        .map(match -> createIndexMatch(alertComposite, match))
        .collect(toList());
  }

  private static IndexMatch createIndexMatch(AlertComposite alertComposite, RegisteredMatch match) {
    return IndexMatch
        .builder()
        .matchId(match.getMatchId())
        .matchName(match.getMatchName())
        .matchingTexts(getMatchingTexts(alertComposite, match))
        .build();
  }

  @Nonnull
  private static String getMatchingTexts(AlertComposite alertComposite, RegisteredMatch match) {
    return String.join(
        ", ",
        alertComposite.getHitById(match.getMatchId()).getMatchingTexts());
  }

}
