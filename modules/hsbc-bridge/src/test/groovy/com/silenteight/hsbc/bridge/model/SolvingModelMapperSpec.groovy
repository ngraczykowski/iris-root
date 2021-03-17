package com.silenteight.hsbc.bridge.model

import com.silenteight.model.api.v1.Feature
import com.silenteight.model.api.v1.SolvingModel

import spock.lang.Specification

class SolvingModelMapperSpec extends Specification {

  def 'should map from grpc SolvingModel class to SolvingModelDto class'() {
    given:
    def feature = Feature.newBuilder().setName("name").setAgentConfig("agentConfig").build()
    def solvingModel = SolvingModel.newBuilder()
        .setName("name")
        .setPolicyName("policy_name")
        .setStrategyName("strategy_name")
        .addAllFeatures(List.of(feature))
        .addAllCategories(List.of("category"))
        .build()

    when:
    def solvingModelDto = SolvingModelMapper.mapToSolvingModelDto(solvingModel)

    then:
    with(solvingModelDto) {
      name == solvingModel.name
      policyName == solvingModel.policyName
      strategyName == solvingModel.strategyName
      features.size() == solvingModel.featuresList.size()
      categories.size() == solvingModel.categoriesList.size()
      features.every {
        it.getName() == solvingModel.featuresList.first().name
        it.getAgentConfig() == solvingModel.featuresList.first().agentConfig
      }
      categories.every {
        it == solvingModel.categoriesList.first()
      }
    }
  }
}
