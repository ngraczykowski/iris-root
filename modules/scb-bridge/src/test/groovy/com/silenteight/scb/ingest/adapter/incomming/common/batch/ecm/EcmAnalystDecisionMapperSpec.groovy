package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm

import com.silenteight.scb.ingest.adapter.incomming.common.quartz.EcmAnalystDecision

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.proto.serp.v1.alert.AnalystSolution.*

class EcmAnalystDecisionMapperSpec extends Specification {

  def mappedAnalystDecisions = [
      new EcmAnalystDecision(text: 'Risk Relevant', solution: ANALYST_TRUE_POSITIVE),
      new EcmAnalystDecision(text: 'Risk Irrelevant', solution: ANALYST_FALSE_POSITIVE)
  ]
  def underTest = new EcmAnalystDecisionMapper(mappedAnalystDecisions)

  @Unroll
  def 'should map analyst decision: #analystDecision into #expectedResult'() {
    when:
    def result = underTest.mapEcmDecision(analystDecision)

    then:
    result == expectedResult

    where:
    analystDecision   | expectedResult
    'Risk Relevant'   | ANALYST_TRUE_POSITIVE
    'Risk Irrelevant' | ANALYST_FALSE_POSITIVE
    null              | ANALYST_OTHER
    ''                | ANALYST_OTHER
    'Risk relevant'   | ANALYST_OTHER
  }
}
