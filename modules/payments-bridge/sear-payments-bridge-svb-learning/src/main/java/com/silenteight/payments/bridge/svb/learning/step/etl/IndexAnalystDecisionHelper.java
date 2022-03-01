package com.silenteight.payments.bridge.svb.learning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.domain.ActionComposite;
import com.silenteight.payments.bridge.svb.learning.mapping.DecisionMapper;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAnalystDecision;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class IndexAnalystDecisionHelper {

  private final DecisionMapper decisionMapper;

  IndexAnalystDecision getDecision(List<ActionComposite> actionComposites) {
    var orderedActions = actionComposites.stream()
        .sorted(Comparator.comparing(ActionComposite::getActionDatetime))
        .collect(Collectors.toCollection(LinkedList::new));
    var current = orderedActions.getLast();

    return new IndexAnalystDecision(
        current.getStatusName(),
        makeDecision(orderedActions, current),
        current.getActionComment(),
        current.getActionDatetime().toString());
  }

  private String makeDecision(List<ActionComposite> orderedActions, ActionComposite current) {
    return decisionMapper.map(
        orderedActions.stream()
            .limit(orderedActions.size() - 1)
            .map(ActionComposite::getStatusName)
            .collect(Collectors.toList()),
        current.getStatusName());
  }

}
