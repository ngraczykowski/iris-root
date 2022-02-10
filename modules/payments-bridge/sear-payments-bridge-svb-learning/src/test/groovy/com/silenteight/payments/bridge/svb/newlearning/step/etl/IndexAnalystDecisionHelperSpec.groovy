package com.silenteight.payments.bridge.svb.newlearning.step.etl

import com.silenteight.payments.bridge.svb.migration.DecisionMapperConfiguration
import com.silenteight.payments.bridge.svb.migration.DecisionMapperFactory
import com.silenteight.payments.bridge.svb.newlearning.domain.ActionComposite

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [DecisionMapperConfiguration.class, DecisionMapperFactory.class,
    IndexAnalystDecisionHelper.class])
class IndexAnalystDecisionHelperSpec extends Specification {

  @Autowired
  private IndexAnalystDecisionHelper indexAnalystDecisionHelper

  def "Empty actions list should throw Exception"() {
    when:
    indexAnalystDecisionHelper.getDecision(List.of())

    then:
    thrown Exception
  }

  def "Action with StatusName prev='#prev' and current='#current', should give '#analyst_decision'"() {
    given:
    def now = OffsetDateTime.now()
    def currentAction = ActionComposite.builder()
        .actionComment("current")
        .statusName(current)
        .actionDatetime(now)
        .actionId(1L)
        .build()
    def prevAction = ActionComposite.builder()
        .actionComment("prev")
        .statusName(prev)
        .actionDatetime(now.minusNanos(1L))
        .actionId(2L)
        .build()

    expect:
    indexAnalystDecisionHelper.getDecision(List.of(prevAction, currentAction)).getDecision() ==
        analyst_decision

    indexAnalystDecisionHelper.getDecision(List.of(currentAction, prevAction)).getDecision() ==
        analyst_decision

    indexAnalystDecisionHelper
        .getDecision(List.of(currentAction, currentAction, prevAction)).getDecision() ==
        analyst_decision

    where:
    prev           | current             | analyst_decision
    '*'            | 'CANCELLED'         | 'analyst_decision_false_positive'
    '*'            | 'FAILED'            | 'analyst_decision_false_positive'
    '*'            | 'GFX_45_DAY'        | 'analyst_decision_false_positive'
    '*'            | 'L1_PASS'           | 'analyst_decision_false_positive'
    '*'            | 'L2_PASS'           | 'analyst_decision_false_positive'
    'L2_L3_PEND'   | 'L3_PASS'           | 'analyst_decision_true_positive'
    'L2_ESC_TO_L3' | 'L3_PASS'           | 'analyst_decision_true_positive'
    '*'            | 'L3_PASS'           | 'analyst_decision_false_positive'
    '*'            | 'MANUAL_PASS'       | 'analyst_decision_false_positive'
    '*'            | 'PASS'              | 'analyst_decision_false_positive'
    '*'            | 'PASSED'            | 'analyst_decision_false_positive'
    '*'            | 'RECHECKED'         | 'analyst_decision_false_positive'
    '*'            | 'L2_CANCEL'         | 'analyst_decision_true_positive'
    '*'            | 'L3_BLOCK'          | 'analyst_decision_true_positive'
    '*'            | 'L3_CANCEL'         | 'analyst_decision_true_positive'
    '*'            | 'L3_REJECT'         | 'analyst_decision_true_positive'
    '*'            | 'EMEA_US_L3'        | 'analyst_decision_pending'
    '*'            | 'ESC_TO_L2'         | 'analyst_decision_pending'
    '*'            | 'FILTER'            | 'analyst_decision_pending'
    '*'            | 'FR_ESC'            | 'analyst_decision_pending'
    '*'            | 'FR_FAIL'           | 'analyst_decision_pending'
    '*'            | 'FR_PASS'           | 'analyst_decision_pending'
    '*'            | 'GFX_45DAY'         | 'analyst_decision_pending'
    '*'            | 'HALF_FAILED'       | 'analyst_decision_pending'
    '*'            | 'HALF_PASSED'       | 'analyst_decision_pending'
    '*'            | 'L2_ESC_TO_L3'      | 'analyst_decision_pending'
    '*'            | 'L2_L3_PEND'        | 'analyst_decision_pending'
    '*'            | 'L3_BLOCK_ECM'      | 'analyst_decision_pending'
    '*'            | 'L3_REJECT_ECM'     | 'analyst_decision_pending'
    '*'            | 'PENDING'           | 'analyst_decision_pending'
    '*'            | 'READY_EMEA_L3'     | 'analyst_decision_pending'
    '*'            | 'READY_US_L3'       | 'analyst_decision_pending'
    '*'            | 'SAN_CONF'          | 'analyst_decision_pending'
    '*'            | '*'                 | 'analyst_decision_unknown'
    'L3_BLOCK'     | '!@@#)(*&)(*&!@#^#' | 'analyst_decision_unknown'
    'L2_L3_PEND'   | '!@@#)(*&)(*&!@#^#' | 'analyst_decision_unknown'
    'L2_ESC_TO_L3' | '!@@#)(*&)(*&!@#^#' | 'analyst_decision_unknown'
  }
}
