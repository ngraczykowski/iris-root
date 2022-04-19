package com.silenteight.qco.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.qco.domain.model.MatchSolution;
import com.silenteight.qco.domain.model.QcoRecommendationAlert;
import com.silenteight.qco.domain.model.QcoRecommendationAlert.QcoMatchData;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class QcoAlertService {

  private final MatchProcessor matchProcessor;

  QcoRecommendationAlert extractAndProcessRecommendationAlert(QcoRecommendationAlert alert) {
    return alert.toBuilder()
        .matches(updateQcoMatchesData(alert))
        .build();
  }

  private List<QcoMatchData> updateQcoMatchesData(QcoRecommendationAlert alert) {
    return alert.matches()
        .stream()
        .map(qcoMatchData -> updateQcoMatchData(qcoMatchData, alert))
        .toList();
  }

  private QcoMatchData updateQcoMatchData(QcoMatchData matchData, QcoRecommendationAlert alert) {
    var qcoRecommendationMatch = QcoAlertMapper.toQcoRecommendationMatch(matchData, alert);
    var matchSolution = matchProcessor.processMatch(qcoRecommendationMatch);

    if (matchSolution.changed()) {
      return overrideRecommendation(matchData, matchSolution);
    }
    return matchData;
  }

  private QcoMatchData overrideRecommendation(QcoMatchData matchData, MatchSolution matchSolution) {
    return matchData.toBuilder()
        .recommendation(matchSolution.solution())
        .comment(matchSolution.comment())
        .build();
  }
}
