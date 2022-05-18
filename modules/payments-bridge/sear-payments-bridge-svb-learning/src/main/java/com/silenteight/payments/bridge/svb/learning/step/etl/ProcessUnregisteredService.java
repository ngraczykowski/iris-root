package com.silenteight.payments.bridge.svb.learning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;
import com.silenteight.payments.bridge.svb.learning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;
import com.silenteight.payments.bridge.svb.learning.domain.LearningRegisteredAlert;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlertIdSet;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlertRequest;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexMatch;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ProcessUnregisteredService {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final IngestDatasourceService ingestDatasourceService;
  private final CreateAlertDataRetentionUseCase createAlertDataRetentionUseCase;

  private final IndexLearningUseCase indexLearningUseCase;
  private final IndexAnalystDecisionHelper indexAnalystDecisionHelper;

  private final ApplicationEventPublisher applicationEventPublisher;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  LearningRegisteredAlert process(AlertComposite alertComposite, long jobId) {
    var registeredAlerts = registerAlertUseCase.batchRegistration(
        List.of(alertComposite.toRegisterAlertRequest(jobId)));

    var registeredAlert = registeredAlerts.get(0);
    applicationEventPublisher.publishEvent(registeredAlert.toLearningAlertRegisteredEvent());

    var learningRegisteredAlert = alertComposite.toLearningRegisteredAlert(registeredAlert);
    ingestDatasourceService.ingest(alertComposite, registeredAlert);
    createAlertDataRetentionUseCase.create(
        List.of(alertComposite.alertDataRetention(registeredAlert)));
    indexLearningUseCase.index(createIndexAlerts(alertComposite, registeredAlert));
    return learningRegisteredAlert;
  }

  private List<IndexAlertRequest> createIndexAlerts(
      AlertComposite alertComposite,
      RegisterAlertResponse registeredAlert) {
    return List.of(
        new IndexAlertRequest(
            new IndexAlertIdSet(
                String.valueOf(alertComposite.getAlertDetails().getAlertId()),
                registeredAlert.getAlertName(),
                alertComposite.getSystemId(),
                alertComposite.getAlertDetails().getMessageId()),
            registeredMatches(alertComposite, registeredAlert),
            indexAnalystDecisionHelper.getDecision(alertComposite.getActions()
            )
        ));
  }

  private static List<IndexMatch> registeredMatches(
      AlertComposite alertComposite, RegisterAlertResponse registeredAlert) {
    return registeredAlert
        .getMatchResponses()
        .stream()
        .map(registerMatchResponse ->
            new IndexMatch(
                registerMatchResponse.getMatchId(),
                registerMatchResponse.getMatchName(),
                matchingText(alertComposite).get(registerMatchResponse.getMatchId())
            ))
        .collect(Collectors.toList());
  }

  private static Map<String, String> matchingText(AlertComposite alertComposite) {
    return alertComposite
        .getHits()
        .stream()
        .collect(Collectors.toMap(
            HitComposite::getMatchId,
            st -> String.join(", ", st.getMatchingTexts())));
  }
}
