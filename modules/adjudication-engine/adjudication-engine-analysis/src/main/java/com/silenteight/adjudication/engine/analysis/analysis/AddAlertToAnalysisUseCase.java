package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisAlert;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded.Builder;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.silenteight.adjudication.engine.analysis.analysis.AnalysisAlertEntity.fromAnalysisAlert;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class AddAlertToAnalysisUseCase {

  private final AnalysisAlertRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "analysis" })
  @Transactional
  List<AnalysisAlert> batchAddAlert(String analysis, List<AnalysisAlert> alerts) {
    var analysisId = ResourceName.create(analysis).getLong("analysis");
    var eventBuilder = AnalysisAlertsAdded.newBuilder();
    var savedAnalysisAlerts = saveAllAlerts(analysisId, alerts, eventBuilder);

    eventPublisher.publishEvent(eventBuilder.build());

    return savedAnalysisAlerts.stream().map(AnalysisAlertEntity::toAnalysisAlert).collect(toList());
  }

  private Collection<AnalysisAlertEntity> saveAllAlerts(
      long analysisId, List<AnalysisAlert> alerts, Builder eventBuilder) {

    return repository.saveAll(
        () -> alerts.stream().map(a -> {
          var entity = fromAnalysisAlert(analysisId, a);
          eventBuilder.addAnalysisAlerts(entity.getName());
          return entity;
        }).iterator());
  }
}
