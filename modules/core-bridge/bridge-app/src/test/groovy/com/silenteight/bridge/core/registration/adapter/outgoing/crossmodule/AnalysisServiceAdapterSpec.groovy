package com.silenteight.bridge.core.registration.adapter.outgoing.crossmodule

import com.silenteight.adjudication.api.library.v1.analysis.*
import com.silenteight.adjudication.api.library.v1.analysis.AddAlertsToAnalysisOut.AddedAlert
import com.silenteight.bridge.core.registration.domain.model.AlertsAddedToAnalysis.Status
import com.silenteight.bridge.core.registration.domain.model.DefaultModel
import com.silenteight.bridge.core.registration.domain.model.DefaultModelFeature

import com.google.protobuf.Timestamp
import spock.lang.Specification
import spock.lang.Subject

import java.time.OffsetDateTime

class AnalysisServiceAdapterSpec extends Specification {

  def analysisServiceClient = Mock(AnalysisServiceClient)

  @Subject
  def underTest = new AnalysisServiceAdapter(analysisServiceClient)

  def "Should create analysis"() {
    given:
    def policyName = "policy_name"
    def strategyName = "strategy_name"

    def createAnalysisOut =
        CreateAnalysisOut.builder()
            .name("analysis_name")
            .policy(policyName)
            .strategy(strategyName)
            .build()

    def defaultModel = DefaultModel.builder()
        .name("model_name")
        .categories(["category1", "category2"])
        .features([DefaultModelFeature.builder().name("feature_name").build()])
        .policyName(policyName)
        .strategyName(strategyName)
        .build()

    and:
    analysisServiceClient.createAnalysis(_ as CreateAnalysisIn) >> createAnalysisOut

    when:
    def result = underTest.create(defaultModel)

    then:
    with(result) {
      name() == createAnalysisOut.name
    }
  }

  def 'should add alerts to analysis'() {
    given:
    def analysisName = 'analysisName'
    def alertName = 'alert_1'
    def alertNames = [alertName]
    def timestamp = Timestamp.newBuilder()
        .setSeconds(OffsetDateTime.now().toEpochSecond())
        .build()
    def response = AddAlertsToAnalysisOut.builder()
        .addedAlerts(
            [AddedAlert.builder()
                 .name(alertName)
                 .createdAt(timestamp)
                 .build()])
        .build()

    when:
    def result = underTest.addAlertsToAnalysis(analysisName, alertNames, timestamp)

    then:
    1 * analysisServiceClient.addAlertsToAnalysis(_ as AddAlertsToAnalysisIn) >> response
    with(result) {
      result.status() == Status.SUCCESS
      result.alertNames().first() == alertName
    }
  }
}
