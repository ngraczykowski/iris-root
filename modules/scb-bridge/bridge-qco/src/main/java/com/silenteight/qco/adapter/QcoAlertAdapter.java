package com.silenteight.qco.adapter;

import lombok.RequiredArgsConstructor;

import com.silenteight.qco.domain.MatchProcessor;
import com.silenteight.qco.domain.model.QcoRecommendationAlert;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QcoAlertAdapter {

  private final MatchProcessor matchProcessor;

  public QcoRecommendationAlert extractAndProcessRecommendationAlert(QcoRecommendationAlert alert) {
    //TODO: call processMatch in loop
    var matchSolution = matchProcessor.processMatch(null);
    return QcoRecommendationAlert.builder().build();
  }
}