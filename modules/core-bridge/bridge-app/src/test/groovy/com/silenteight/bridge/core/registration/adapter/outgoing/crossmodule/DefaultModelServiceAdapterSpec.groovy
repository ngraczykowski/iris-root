package com.silenteight.bridge.core.registration.adapter.outgoing.crossmodule

import com.silenteight.governance.api.library.v1.model.FeatureOut
import com.silenteight.governance.api.library.v1.model.ModelServiceClient
import com.silenteight.governance.api.library.v1.model.SolvingModelOut

import spock.lang.Specification
import spock.lang.Subject

class DefaultModelServiceAdapterSpec extends Specification {

  def modelServiceClient = Mock(ModelServiceClient)

  @Subject
  def underTest = new DefaultModelServiceAdapter(modelServiceClient)

  def "should return default model"() {
    given:
    def solvingModel = SolvingModelOut.builder()
        .name("solvingModels/mock")
        .policyName("policies/mock")
        .strategyName("strategies/mock")
        .categories(["mockCategory"])
        .features(
            [
                FeatureOut.builder()
                    .name("feature/mock")
                    .agentConfig("agents/mock")
                    .build()
            ])
        .build()
    modelServiceClient.getSolvingModel() >> solvingModel

    when:
    def result = underTest.getForSolving()

    then:
    with(result) {
      name() == solvingModel.name
      policyName() == solvingModel.policyName
      strategyName() == solvingModel.strategyName
      categories() == solvingModel.categories
      features().size() == solvingModel.features.size()
      features()[0].name() == solvingModel.features[0].name
      features()[0].agentConfig() == solvingModel.features[0].agentConfig
    }
  }
}
