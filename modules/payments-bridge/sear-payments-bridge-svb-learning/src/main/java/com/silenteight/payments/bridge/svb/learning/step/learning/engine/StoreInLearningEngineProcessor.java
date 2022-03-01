package com.silenteight.payments.bridge.svb.learning.step.learning.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.learning.mapping.DecisionMapper;

import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class StoreInLearningEngineProcessor implements
    ItemProcessor<AlertComposite, HistoricalDecisionLearningAggregate> {

  private final DecisionMapper decisionMapper;
  private final List<String> discriminators;

  @Override
  public HistoricalDecisionLearningAggregate process(
      AlertComposite item) throws Exception {
    if (log.isDebugEnabled()) {
      log.debug(
          "Processing item alertId: {} systemId: {}", item.getAlertDetails().getAlertId(),
          item.getAlertDetails().getSystemId());
    }
    var requests = discriminators.stream().map(discriminator ->
        item.toHistoricalDecisionRequest(decisionMapper, discriminator)
    ).collect(Collectors.toList());
    return HistoricalDecisionLearningAggregate
        .builder().historicalFeatureRequests(requests)
        .build();
  }
}
