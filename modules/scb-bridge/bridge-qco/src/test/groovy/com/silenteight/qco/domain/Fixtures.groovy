package com.silenteight.qco.domain

import com.silenteight.qco.domain.model.ChangeCondition
import com.silenteight.qco.domain.model.QcoParams
import com.silenteight.qco.domain.model.QcoPolicyStepSolutionOverrideConfiguration
import com.silenteight.qco.domain.model.QcoRecommendationMatch

class Fixtures {

  static CHANGE_CONDITION = new ChangeCondition("policyId", "stepId", "solution")
  static UNEXPECTED_CHANGE_CONDITION = new ChangeCondition("policyId1", "stepId", "solution")
  static QCO_PARAM_1 = new QcoParams(1, "solution 2")
  static QCO_RECOMMENDATION_MATCH = QcoRecommendationMatch.builder()
      .batchId("batchId")
      .policyId("policyId")
      .stepId("stepId")
      .solution("solution")
      .comment("comment")
      .matchName("matchName")
      .alertId("alertId")
      .alertName("alertName")
      .build()
  static QCO_PARAM_2 = new QcoParams(2, "solution 2")
  static QCO_CONFIGURATION_1 =
      new QcoPolicyStepSolutionOverrideConfiguration(
          "policyId", "stepId", 3, "solution", "override")
  static QCO_CONFIGURATION_2 =
      new QcoPolicyStepSolutionOverrideConfiguration(
          "policyId2", "stepId2", 4, "solution2", "override2")
  static QCO_CONFIGURATION_DUPLICATED_VALUES =
      new QcoPolicyStepSolutionOverrideConfiguration(
          "policyId", "stepId", 7, "solution", "new solution")
}