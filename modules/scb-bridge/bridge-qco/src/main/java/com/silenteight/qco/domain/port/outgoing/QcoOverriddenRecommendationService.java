package com.silenteight.qco.domain.port.outgoing;

import com.silenteight.qco.domain.model.MatchSolution;
import com.silenteight.qco.domain.model.QcoRecommendationMatch;

public interface QcoOverriddenRecommendationService {

  void storeQcoOverriddenRecommendation(
      QcoRecommendationMatch match, MatchSolution matchSolution);
}
