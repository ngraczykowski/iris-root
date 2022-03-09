package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.proto.serp.v1.alert.AnalystSolution
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant
import java.time.temporal.ChronoField

import static com.silenteight.proto.serp.v1.alert.AnalystSolution.ANALYST_FALSE_POSITIVE
import static com.silenteight.proto.serp.v1.alert.AnalystSolution.ANALYST_NO_SOLUTION
import static java.util.Optional.empty
import static java.util.Optional.of

class AlertRecordCompositeSpec extends Specification {

  @Unroll
  def 'should get last reset decision date results in #expectedResult for #decisionRecords'() {
    given:
    def alertRecordComposite = alertRecordComposite(decisionRecords)

    when:
    def result = alertRecordComposite.getLastResetDecisionDate()

    then:
    result == expectedResult

    where:
    decisionRecords                                          | expectedResult
    []                                                       | empty()
    [decision(now, analystOperator, ANALYST_FALSE_POSITIVE)] | empty()
    [decision(now, systemOperator, ANALYST_FALSE_POSITIVE)]  | empty()
    [decision(now, systemOperator, ANALYST_NO_SOLUTION)]     | of(now)
    [decision(now, analystOperator, ANALYST_NO_SOLUTION)]    | empty()
    [
        decision(somePastDate, systemOperator, ANALYST_NO_SOLUTION),
        decision(now, systemOperator, ANALYST_NO_SOLUTION)
    ]                                                        | of(now)
    [
        decision(now, analystOperator, ANALYST_NO_SOLUTION),
        decision(somePastDate, systemOperator, ANALYST_NO_SOLUTION)
    ]                                                        | of(somePastDate)
  }

  def 'should get last decision'() {
    when: 'empty list'
    def result = alertRecordComposite([]).getLastDecision()

    then: 'return empty'
    result.isEmpty()

    when: 'single analyst decision on the list'
    result = alertRecordComposite(
        [
            decision(now, analystOperator, ANALYST_NO_SOLUTION)
        ]).getLastDecision()

    then: 'return analyst decision'
    result.isPresent()

    when: 'no decision date on the list'
    result = alertRecordComposite(
        [
            decision(null, analystOperator, ANALYST_NO_SOLUTION),
            decision(null, systemOperator, ANALYST_NO_SOLUTION)
        ]).getLastDecision()

    then: 'return empty'
    result.isEmpty()

    when: 'two system decisions'
    result = alertRecordComposite(
        [
            decision(somePastDate, systemOperator, ANALYST_NO_SOLUTION),
            decision(now, systemOperator, ANALYST_NO_SOLUTION)
        ]).getLastDecision()

    then: 'return empty'
    result.isEmpty()

    when: 'two analyst decisions'
    result = alertRecordComposite(
        [
            decision(somePastDate, analystOperator, ANALYST_FALSE_POSITIVE),
            decision(somePastDate, analystOperator, ANALYST_NO_SOLUTION),
            decision(now, analystOperator, ANALYST_NO_SOLUTION)
        ]).getLastDecision()

    then: 'return the newest decision'
    result.isPresent()
    result.get().getCreatedAt().getSeconds() == now.getLong(ChronoField.INSTANT_SECONDS)
  }

  def decision(Instant decisionDate, String operator, AnalystSolution solution) {
    DecisionRecord.builder()
        .decisionDate(decisionDate)
        .operator(operator)
        .systemId('')
        .solution(solution)
        .build()
  }

  def alertRecordComposite(List<DecisionRecord> decisions) {
    AlertRecordComposite.builder()
        .alert(alert())
        .cbsHitDetails([])
        .decisions(decisions)
        .build()
  }

  def alert() {
    AlertRecord.builder()
        .systemId('')
        .build()
  }

  @Shared
  def somePastDate = Instant.ofEpochSecond(100)
  @Shared
  def now = Instant.now()
  @Shared
  def systemOperator = 'FSK'
  @Shared
  def analystOperator = 'ABC'
}
