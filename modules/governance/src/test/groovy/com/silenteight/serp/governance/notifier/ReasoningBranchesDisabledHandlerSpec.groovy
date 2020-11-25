package com.silenteight.serp.governance.notifier

import com.silenteight.proto.serp.v1.common.ObjectId
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId
import com.silenteight.proto.serp.v1.governance.ReasoningBranchesDisabledEvent
import com.silenteight.proto.serp.v1.governance.ReasoningBranchesDisabledEvent.Cause
import com.silenteight.protocol.utils.Uuids
import com.silenteight.serp.governance.solutiondiscrepancy.SolutionDiscrepancyProperties

import spock.lang.Specification

import java.time.Instant

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp

class ReasoningBranchesDisabledHandlerSpec extends Specification {

  public static final UUID correlationId = UUID.fromString("01256804-1ce1-4d52-94d4-d1876910f272")

  def 'should notify about disabled reasoning branch'() {
    given:
    ReasoningBranchesDisabledHandler underTest = createRBDisabledHandler(false)
    ReasoningBranchesDisabledEvent event = createEvent()

    when:
    def result = underTest.notifyReasoningBranchesDisabled(event)

    then:
    result.subject ==
        'Reasoning Branches Disabled - Alert HK_BTCH_DENY!57535030-D413B044-C999F2CD-075A3457'
    result.body == 'SERP Circuit Breaker has triggered ' +
        'with ID - 01256804-1ce1-4d52-94d4-d1876910f272 ' +
        'on 2020-07-08 22:28:15 UTC ' +
        'due to alert HK_BTCH_DENY!57535030-D413B044-C999F2CD-075A3457 ' +
        'and has disabled below Reasoning Branch(es):\n' +
        '\n' +
        ' * 1-2\n' +
        ' * 1-3\n' +
        '\n' +
        'This is an Auto Generated Mail. Please do not reply.'
  }

  def 'should notify about disabled reasoning branch in dry-run mode'() {
    given:
    ReasoningBranchesDisabledHandler underTest = createRBDisabledHandler(true)
    ReasoningBranchesDisabledEvent event = createEvent()

    when:
    def result = underTest.notifyReasoningBranchesDisabled(event)

    then:
    result.subject == '[DRY-RUN] Reasoning Branches Disabled - ' +
        'Alert HK_BTCH_DENY!57535030-D413B044-C999F2CD-075A3457'
    result.body == 'SERP Circuit Breaker [DRY RUN] has triggered ' +
        'with ID - 01256804-1ce1-4d52-94d4-d1876910f272 ' +
        'on 2020-07-08 22:28:15 UTC ' +
        'due to alert HK_BTCH_DENY!57535030-D413B044-C999F2CD-075A3457 ' +
        'and has disabled below Reasoning Branch(es):\n' +
        '\n' +
        ' * 1-2\n' +
        ' * 1-3\n' +
        '\n' +
        'This is an Auto Generated Mail. Please do not reply.'
  }

  private static ReasoningBranchesDisabledHandler createRBDisabledHandler(boolean enabled) {
    def properties = new SolutionDiscrepancyProperties();
    properties.setDryRunEnabled(enabled);
    def configuration = new ReasoningBranchesDisabledHandlerConfiguration(properties);
    def underTest = configuration.reasoningBranchesDisabledHandler();
    underTest
  }

  private static ReasoningBranchesDisabledEvent createEvent() {
    def cause = Cause.newBuilder()
        .setDescription('Alert HK_BTCH_DENY!57535030-D413B044-C999F2CD-075A3457')
        .setAlertId(
            ObjectId.newBuilder().setSourceId("HK_BTCH_DENY!57535030-D413B044-C999F2CD-075A3457"))
        .build()
    def reasoningBranch1 = ReasoningBranchId.newBuilder()
        .setDecisionTreeId(1)
        .setFeatureVectorId(2)
        .build()
    def reasoningBranch2 = ReasoningBranchId.newBuilder()
        .setDecisionTreeId(1)
        .setFeatureVectorId(3)
        .build()
    def reasoningBranch3 = ReasoningBranchId.newBuilder()
        .setDecisionTreeId(1)
        .setFeatureVectorId(2)
        .build()
    def event = ReasoningBranchesDisabledEvent.newBuilder()
        .setCause(cause)
        .addAllReasoningBranches([reasoningBranch1, reasoningBranch2, reasoningBranch3])
        .setCreatedAt(toTimestamp(Instant.parse("2020-07-08T22:28:15Z")))
        .setCorrelationId(Uuids.fromJavaUuid(correlationId))
        .build()
    event
  }
}
