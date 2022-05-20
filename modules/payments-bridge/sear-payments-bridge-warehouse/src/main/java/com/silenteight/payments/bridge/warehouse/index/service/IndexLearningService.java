package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlertRequest;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAnalystDecision;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;
import com.silenteight.payments.bridge.warehouse.index.service.IndexedAlertBuilderFactory.AlertBuilder;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class IndexLearningService implements IndexLearningUseCase {

  private final IndexedAlertBuilderFactory alertBuilderFactory;
  private final IndexAlertUseCase indexAlertUseCase;
  private final LearningWarehouseMapper warehouseMapper;

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public void index(List<IndexAlertRequest> learningAlerts) {
    var alerts = learningAlerts.stream()
        .map(learningAlert -> {
          var alertIdSet = learningAlert.getAlertIdSet();
          var alertBuilder = createIndexAlertBuilder(
              alertIdSet.getDiscriminator(),
              alertIdSet.getAlertName(),
              learningAlert.getDecision())
              .addPayload(warehouseMapper.makeAlert(
                  alertIdSet.getAlertId(), alertIdSet.getSystemId()));
          learningAlert.getMatches().forEach(m -> alertBuilder
              .newMatch()
              .setName(m.getMatchName())
              .setDiscriminator(m.getMatchName())
              .addPayload(warehouseMapper.makeMatch(m))
              .finish());
          return alertBuilder.build();
        })
        .collect(toList());
    indexAlertUseCase.index(alerts, RequestOrigin.LEARNING);
  }

  private AlertBuilder createIndexAlertBuilder(String discriminator,
      String alertName, IndexAnalystDecision decision) {
    return alertBuilderFactory.newBuilder()
        .setDiscriminator(discriminator)
        .setName(alertName)
        .addPayload(warehouseMapper.makeAnalystDecision(decision));
  }

}
