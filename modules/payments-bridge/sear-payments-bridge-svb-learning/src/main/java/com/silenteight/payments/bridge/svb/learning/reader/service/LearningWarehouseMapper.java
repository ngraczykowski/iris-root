package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.AnalystDecision;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseAlert;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseAnalystSolution;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseMatch;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class LearningWarehouseMapper {

  private final DecisionMapper decisionMapper;

  WarehouseAlert makeAlert(LearningAlert learningAlert) {
    return WarehouseAlert.builder()
        .alertMessageId(learningAlert.getAlertId())
        .fircoSystemId(learningAlert.getSystemId())
        .build();
  }

  WarehouseAnalystSolution makeAnalystDecision(AnalystDecision analystDecision) {
    var decision = decisionMapper.map(analystDecision.getStatus());

    return WarehouseAnalystSolution.builder()
        .fircoAnalystStatus(analystDecision.getStatus())
        .fircoAnalystDecision(decision)
        .fircoAnalystComment(analystDecision.getComment())
        .fircoAnalystDecisionTime(analystDecision.getActionDateTimeAsString())
        .build();
  }

  WarehouseMatch makeMatch(LearningMatch match) {
    return WarehouseMatch.builder()
        .matchId(match.getMatchId())
        .matchingText(String.join(", ", match.getMatchingTexts()))
        .build();
  }
}
