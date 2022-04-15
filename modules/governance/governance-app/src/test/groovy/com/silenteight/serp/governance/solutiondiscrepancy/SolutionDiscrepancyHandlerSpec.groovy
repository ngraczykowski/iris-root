package com.silenteight.serp.governance.solutiondiscrepancy

import com.silenteight.proto.serp.v1.circuitbreaker.SolutionDiscrepancyDetectedEvent
import com.silenteight.proto.serp.v1.circuitbreaker.SolutionDiscrepancyDetectedEvent.AnalystDecision
import com.silenteight.proto.serp.v1.common.ObjectId
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId
import com.silenteight.protocol.utils.Uuids
import com.silenteight.sep.base.testing.time.MockTimeSource
import com.silenteight.serp.governance.branch.BranchService
import com.silenteight.serp.governance.branch.ConfigureBranchRequest

import spock.lang.Specification

import static com.silenteight.protocol.utils.MoreTimestamps.toInstant

class SolutionDiscrepancyHandlerSpec extends Specification {

  public static final MockTimeSource timeSource = MockTimeSource.ARBITRARY_INSTANCE
  def branchService = Mock(BranchService)

  def 'should handle SolutionDiscrepancyDetectedEvent'() {
    given:
    def underTest = new SolutionDiscrepancyHandler(branchService, false, timeSource)

    def someCorrelationId = Uuids.random()
    def someReasoningBranch = ReasoningBranchId.newBuilder()
        .setDecisionTreeId(1)
        .setFeatureVectorId(2)
        .build()
    def alertId = ObjectId.newBuilder().setSourceId('alert-id').build()
    def event = SolutionDiscrepancyDetectedEvent.newBuilder()
        .addAllReasoningBranches([someReasoningBranch])
        .setCorrelationId(someCorrelationId)
        .setDecision(AnalystDecision.newBuilder().setAlertId(alertId).build())
        .build()

    when:
    def result = underTest.disableReasoningBranches(event)

    then:
    1 * branchService.bulkUpdateBranches({List<ConfigureBranchRequest> l -> l.size() == 1})
    result.cause.description == 'Alert alert-id'
    result.cause.alertId == alertId
    result.reasoningBranchesCount == 1
    result.correlationId == someCorrelationId
    toInstant(result.createdAt) == timeSource.now()
  }

  def 'should properly handle SolutionDiscrepancyDetectedEvent in dry-run mode' () {
    given:
    def underTest = new SolutionDiscrepancyHandler(branchService, true, timeSource)

    def someCorrelationId = Uuids.random()
    def someReasoningBranch = ReasoningBranchId.newBuilder()
        .setDecisionTreeId(1)
        .setFeatureVectorId(2)
        .build()
    def alertId = ObjectId.newBuilder().setSourceId('alert-id').build()
    def event = SolutionDiscrepancyDetectedEvent.newBuilder()
        .addAllReasoningBranches([someReasoningBranch])
        .setCorrelationId(someCorrelationId)
        .setDecision(AnalystDecision.newBuilder().setAlertId(alertId).build())
        .build()

    when:
    def result = underTest.disableReasoningBranches(event)

    then:
    result.cause.description == 'Alert alert-id'
    result.cause.alertId == alertId
    result.reasoningBranchesCount == 1
    result.correlationId == someCorrelationId
    toInstant(result.createdAt) == timeSource.now()
  }
}
