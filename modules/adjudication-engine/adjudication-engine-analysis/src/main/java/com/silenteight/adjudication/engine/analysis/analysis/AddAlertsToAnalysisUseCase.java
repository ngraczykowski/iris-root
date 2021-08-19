package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisAlert;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.AddedAnalysisAlerts;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.adjudication.engine.analysis.analysis.AnalysisAlertEntity.fromAnalysisAlert;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class AddAlertsToAnalysisUseCase {

  @NonNull
  private final AnalysisAlertRepository repository;

  @NonNull
  private final ApplicationEventPublisher applicationEventPublisher;

  List<AnalysisAlert> addAlerts(String analysis, List<AnalysisAlert> alerts) {
    long analysisId = ResourceName.create(analysis).getLong("analysis");
    var eventBuilder = AddedAnalysisAlerts.newBuilder();

    var savedAnalysisAlerts = alerts.stream().map(a -> {
      var entity = fromAnalysisAlert(analysisId, a);
      eventBuilder.addAnalysisAlerts(entity.toAlertName());
      return repository.save(entity);
    }).collect(toList());

    applicationEventPublisher.publishEvent(eventBuilder.build());

    return savedAnalysisAlerts.stream().map(AnalysisAlertEntity::toAnalysisAlert).collect(toList());
  }
}
