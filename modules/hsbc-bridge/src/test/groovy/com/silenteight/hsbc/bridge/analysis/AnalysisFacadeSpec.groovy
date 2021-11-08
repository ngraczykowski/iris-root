package com.silenteight.hsbc.bridge.analysis

import com.silenteight.hsbc.bridge.analysis.dto.AnalysisDto

import spock.lang.Specification

import java.time.OffsetDateTime

class AnalysisFacadeSpec extends Specification {

  def repository = Mock(AnalysisRepository)
  def registerer = Mock(Registerer)
  def timeoutCalculator = Mock(AnalysisTimeoutCalculator)
  def underTest = new AnalysisFacade(repository, registerer, timeoutCalculator)

  def fixtures = new Fixtures()

  def 'should create analysis with dataset'() {
    given:
    def dataset = 'dataset'

    when:
    var result = underTest.createAnalysisWithDataset(dataset)

    then:
    1 * registerer.registerAnalysis(dataset) >> fixtures.analysis
    1 * timeoutCalculator.determineTimeout(fixtures.analysis.alertCount) >> OffsetDateTime.now()
    1 * repository.save({AnalysisEntity entity ->
              entity.name == fixtures.analysisName && Objects.nonNull(entity.timeoutAt)})

    with(result) {
      policy == fixtures.policy
      strategy == fixtures.strategy
      name == fixtures.analysisName
    }
  }

  class Fixtures {

    String policy = 'policy-1'
    String strategy = 'strategy-1'

    String analysisName = 'analysis/1'
    AnalysisDto analysis = AnalysisDto.builder()
        .name(analysisName)
        .policy(policy)
        .strategy(strategy)
        .build()
  }
}
