package com.silenteight.qco.domain

import com.silenteight.qco.domain.model.*
import com.silenteight.qco.domain.model.QcoRecommendationAlert.QcoMatchData

class Fixtures {

  static ALERT_NAME = "alertName"
  static ALERT_ID = "alertId"
  static BATCH_ID = "batchId"
  static POLICY_ID = "policyId"
  static MATCH_NAME = "matchName"
  static STEP_ID = "stepId"
  static SOLUTION = "FALSE:POSITIVE"
  static QCO_SOLUTION = "Manual:Investigation"
  static COMMENT = "comment"
  static QCO_COMMENT = "qcoComment"

  static CHANGE_CONDITION = new ChangeCondition("policyId", "stepId", "solution")
  static UNEXPECTED_CHANGE_CONDITION = new ChangeCondition("policyId1", "stepId", "solution")
  static QCO_PARAM_1 = new QcoParams(1, QCO_SOLUTION)
  static QCO_PARAM_2 = new QcoParams(2, "solution 2")
  static QCO_RECOMMENDATION_MATCH = QcoRecommendationMatch.builder()
      .batchId(BATCH_ID)
      .policyId(POLICY_ID)
      .stepId(STEP_ID)
      .solution(SOLUTION)
      .comment(COMMENT)
      .matchName(MATCH_NAME)
      .alertId(ALERT_ID)
      .alertName(ALERT_NAME)
      .build()

  static QCO_CONFIGURATION_1 =
      new QcoPolicyStepSolutionOverrideConfiguration(
          "policyId", "stepId", 3, "solution", "override")
  static QCO_CONFIGURATION_2 =
      new QcoPolicyStepSolutionOverrideConfiguration(
          "policyId2", "stepId2", 4, "solution2", "override2")
  static QCO_CONFIGURATION_DUPLICATED_VALUES =
      new QcoPolicyStepSolutionOverrideConfiguration(
          "policyId", "stepId", 7, "solution", "new solution")

  static MATCH_SOLUTION = new MatchSolution(QCO_SOLUTION, QCO_COMMENT, true)
  static QCO_MATCH_DATA = QcoMatchData.builder()
      .name(MATCH_NAME)
      .stepId(STEP_ID)
      .recommendation(SOLUTION)
      .comment(COMMENT)
      .build()

  static QCO_RECOMMENDATION_ALERT = QcoRecommendationAlert.builder()
      .alertName(ALERT_NAME)
      .batchId(BATCH_ID)
      .policyId(POLICY_ID)
      .alertId(ALERT_ID)
      .matches([QCO_MATCH_DATA])
      .build()
}
