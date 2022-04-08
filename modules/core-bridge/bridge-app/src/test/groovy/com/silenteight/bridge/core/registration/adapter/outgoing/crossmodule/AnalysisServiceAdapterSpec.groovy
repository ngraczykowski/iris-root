package com.silenteight.bridge.core.registration.adapter.outgoing.crossmodule

import com.silenteight.adjudication.api.library.v1.analysis.AnalysisServiceClient
import com.silenteight.adjudication.api.library.v1.analysis.CreateAnalysisIn
import com.silenteight.adjudication.api.library.v1.analysis.CreateAnalysisOut
import com.silenteight.bridge.core.registration.domain.model.DefaultModel
import com.silenteight.bridge.core.registration.domain.model.DefaultModelFeature

import spock.lang.Specification
import spock.lang.Subject

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
}
