/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz.EcmAnalystDecision

import spock.lang.Specification
import spock.lang.Unroll

class EcmAnalystDecisionMapperSpec extends Specification {

  def mappedAnalystDecisions = [
      new EcmAnalystDecision(
          text: 'Risk Relevant', solution: AnalystSolution.ANALYST_TRUE_POSITIVE),
      new EcmAnalystDecision(
          text: 'Risk Irrelevant', solution: AnalystSolution.ANALYST_FALSE_POSITIVE)
  ]
  def underTest = new EcmAnalystDecisionMapper(mappedAnalystDecisions)

  @Unroll
  def 'should map analyst decision: #analystDecision into #expectedResult'() {
    when:
    def result = underTest.mapEcmDecision(analystDecision)

    then:
    result == expectedResult

    where:
    analystDecision | expectedResult
    'Risk Relevant' | AnalystSolution.ANALYST_TRUE_POSITIVE
    'Risk Irrelevant' | AnalystSolution.ANALYST_FALSE_POSITIVE
    null | AnalystSolution.ANALYST_OTHER
    '' | AnalystSolution.ANALYST_OTHER
    'Risk relevant' | AnalystSolution.ANALYST_OTHER
  }
}
