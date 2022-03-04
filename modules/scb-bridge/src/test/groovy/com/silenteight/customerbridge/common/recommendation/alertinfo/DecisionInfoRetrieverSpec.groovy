package com.silenteight.customerbridge.common.recommendation.alertinfo

import com.silenteight.proto.serp.scb.v1.ScbDecisionDetails
import com.silenteight.proto.serp.v1.alert.AnalystSolution
import com.silenteight.proto.serp.v1.alert.Decision
import com.silenteight.sep.base.common.protocol.AnyUtils

import com.google.protobuf.Timestamp
import spock.lang.Specification

import static com.silenteight.proto.serp.v1.alert.AnalystSolution.*

class DecisionInfoRetrieverSpec extends Specification {

  def 'should return empty result'() {
    when:
    def result = DecisionInfoRetriever.getLastIntermediateStateName(decisions)

    then:
    result.isEmpty()

    where:
    decisions << [
        [],
        [createDecision(ANALYST_NO_SOLUTION, 1)],
        [createDecision(ANALYST_OTHER, 1)],
        [createDecision(ANALYST_OTHER, 1), createDecision(ANALYST_OTHER, 2)]
    ]
  }

  def 'should return last intermediate state name'() {
    given:
    def decisions = [
        createDecision(ANALYST_NO_SOLUTION, 1),
        createDecision(ANALYST_OTHER, 2, 'NEW'),
        createDecision(ANALYST_OTHER, 3, 'eOPS'),
        createDecision(ANALYST_FALSE_POSITIVE, 4)
    ]

    when:
    def result = DecisionInfoRetriever.getLastIntermediateStateName(decisions)

    then:
    result.isPresent()
    result.get() == 'eOPS'
  }

  def createDecision(AnalystSolution solution, int seconds) {
    Decision
        .newBuilder()
        .setSolution(solution)
        .setCreatedAt(createTimestamp(seconds))
        .build();
  }

  def createDecision(AnalystSolution solution, int seconds, String stateName) {
    Decision
        .newBuilder()
        .setSolution(solution)
        .setCreatedAt(createTimestamp(seconds))
        .setDetails(AnyUtils.pack(createDetails(stateName)))
        .build();
  }

  def createTimestamp(int seconds) {
    Timestamp.newBuilder().setSeconds(seconds).build();
  }

  def createDetails(String stateName) {
    ScbDecisionDetails.newBuilder().setStateName(stateName).build()
  }
}
