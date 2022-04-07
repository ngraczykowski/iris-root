package com.silenteight.qco.domain

import com.silenteight.qco.domain.model.ChangeCondition
import com.silenteight.qco.domain.model.QcoRecommendationMatch

class Fixtures {
  static CHANGE_CONDITION =  new ChangeCondition("policyId", "stepId", "solution")
  static QCO_RECOMMENDATION_MATCH =  new QcoRecommendationMatch("policyId", "stepId", "solution")
}