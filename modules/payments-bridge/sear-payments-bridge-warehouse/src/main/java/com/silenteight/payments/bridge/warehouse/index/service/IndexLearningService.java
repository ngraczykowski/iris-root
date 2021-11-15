package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlert;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAnalystDecision;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexRegisteredAlert;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;
import com.silenteight.payments.bridge.warehouse.index.service.IndexedAlertBuilderFactory.AlertBuilder;

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
  public void indexForLearning(List<IndexRegisteredAlert> indexRegisterAlertRequest) {
    var alerts = indexRegisterAlertRequest.stream()
        .map(indexAlert -> {
          var alertIds = indexAlert.getAlertIdSet();
          var alertBuilder = createIndexAlertBuilder(
              alertIds.getDiscriminator(),
              alertIds.getAlertName(), indexAlert.getDecision());
          indexAlert.getMatchNames().forEach(matchName -> alertBuilder
              .newMatch()
              .setName(matchName)
              .setDiscriminator(matchName)
              .finish());
          return alertBuilder.build();
        })
        .collect(toList());
    indexAlertUseCase.index(alerts, RequestOrigin.LEARNING);
  }

  @Override
  public void index(List<IndexAlert> learningAlerts) {
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
