package com.silenteight.qco.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.MatchProcessor;
import com.silenteight.qco.domain.model.QcoRecommendationAlert;
import com.silenteight.qco.domain.model.QcoRecommendationMatch;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QcoAlertAdapter {

  private final MatchProcessor matchProcessor;

  public QcoRecommendationAlert extractAndProcessRecommendationAlert(QcoRecommendationAlert alert) {
    log.debug(alert.toString());
    //TODO: call processMatch in loop
    matchProcessor.processMatch(QcoRecommendationMatch.builder()
        .batchId("batch1")
        .alertName("alertName")
        .matchName("matchName")
        .comment("comment")
        .alertId("alertId")
        .policyId("policyId")
        .stepId("stepId")
        .solution("FALSE:POSITIVE")
        .build());
    return QcoRecommendationAlert.builder().build();
  }
}