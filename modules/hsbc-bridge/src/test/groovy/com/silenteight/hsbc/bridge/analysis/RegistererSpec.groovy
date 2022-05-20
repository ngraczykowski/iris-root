package com.silenteight.hsbc.bridge.analysis

import com.silenteight.hsbc.bridge.analysis.dto.AddDatasetRequestDto
import com.silenteight.hsbc.bridge.analysis.dto.AnalysisDatasetDto
import com.silenteight.hsbc.bridge.analysis.dto.CreateAnalysisRequestDto
import com.silenteight.hsbc.bridge.analysis.dto.CreateAnalysisResponseDto
import com.silenteight.hsbc.bridge.model.ModelServiceClient
import com.silenteight.hsbc.bridge.model.dto.SolvingModelDto

import spock.lang.Specification

class RegistererSpec extends Specification {

  def analysisServiceApi = Mock(AnalysisServiceClient)
  def modelClient = Mock(ModelServiceClient)
  def underTest = new Registerer(analysisServiceApi, modelClient)

  def fixtures = new Fixtures()

  def 'should register analysis with dataset'() {
    given:
    def dataset = 'dataset'

    when:
    var result = underTest.registerAnalysis(dataset)

    then:
    1 * analysisServiceApi.createAnalysis(_ as CreateAnalysisRequestDto) >> fixtures.analysis
    1 * analysisServiceApi.addDataset({AddDatasetRequestDto req -> req.dataset == dataset}) >>
        fixtures.datasetDto
    1 * modelClient.getSolvingModel() >> fixtures.solvingModel

    with(result) {
      policy == fixtures.policy
      strategy == fixtures.strategy
      name == fixtures.analysisName
    }
  }

  class Fixtures {

    String policy = 'policy-1'
    String strategy = 'strategy-1'

    SolvingModelDto solvingModel = SolvingModelDto.builder()
        .name('model/1')
        .policyName(policy)
        .strategyName(strategy)
        .features([])
        .build()

    String analysisName = 'analysis/1'
    CreateAnalysisResponseDto analysis = CreateAnalysisResponseDto.builder()
        .name(analysisName)
        .policy(policy)
        .strategy(strategy)
        .build()

    AnalysisDatasetDto datasetDto = AnalysisDatasetDto.builder()
        .alertsCount(1L)
        .name(analysisName)
        .build()
  }
}
