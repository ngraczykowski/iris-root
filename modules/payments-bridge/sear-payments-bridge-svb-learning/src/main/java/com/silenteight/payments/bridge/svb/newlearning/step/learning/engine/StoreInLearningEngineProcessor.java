package com.silenteight.payments.bridge.svb.newlearning.step.learning.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.app.AgentsUtils;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;

import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
@Slf4j
class StoreInLearningEngineProcessor implements
    ItemProcessor<AlertComposite, HistoricalDecisionLearningAggregate> {

  @Override
  public HistoricalDecisionLearningAggregate process(
      AlertComposite item) throws Exception {
    if (log.isDebugEnabled()) {
      log.debug(
          "Processing item alertId: {} systemId: {}", item.getAlertDetails().getAlertId(),
          item.getAlertDetails().getSystemId());
    }
    return HistoricalDecisionLearningAggregate.builder()
        .historicalFeatureRequest(item.toHistoricalDecisionRequest(
            AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC))
        .historicalFeatureRequest(item.toHistoricalDecisionRequest(
            AgentsUtils.HISTORICAL_RISK_CUSTOMER_NAME_LEARNING_DISC))
        .build();
  }
}
