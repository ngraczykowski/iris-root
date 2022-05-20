package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAnalystDecision;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexMatch;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseAlert;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseAnalystSolution;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseMatch;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class LearningWarehouseMapper {

  WarehouseAlert makeAlert(String alertId, String systemId) {
    return WarehouseAlert.builder()
        .alertMessageId(alertId)
        .fircoSystemId(systemId)
        .build();
  }

  WarehouseAnalystSolution makeAnalystDecision(IndexAnalystDecision analystDecision) {
    return WarehouseAnalystSolution.builder()
        .fircoAnalystStatus(analystDecision.getStatus())
        .fircoAnalystDecision(analystDecision.getDecision())
        .fircoAnalystComment(analystDecision.getComment())
        .fircoAnalystDecisionTime(analystDecision.getActionDateTime())
        .build();
  }

  WarehouseMatch makeMatch(IndexMatch match) {
    return WarehouseMatch.builder()
        .matchId(match.getMatchId())
        .matchingText(String.join(", ", match.getMatchingTexts()))
        .build();
  }
}
