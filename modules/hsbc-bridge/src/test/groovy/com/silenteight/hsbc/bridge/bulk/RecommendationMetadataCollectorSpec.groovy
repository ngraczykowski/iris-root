package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.recommendation.metadata.FeatureMetadata
import com.silenteight.hsbc.bridge.recommendation.metadata.MatchMetadata
import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata

import spock.lang.Specification

class RecommendationMetadataCollectorSpec extends Specification {

  def underTest = new RecommendationMetadataCollector()
  def fixtures = new Fixtures()

  def 'should not collect metadata from recommendation metadata when list is null'() {
    given:
    def recommendationMetadata = new RecommendationMetadata()

    when:
    def result = underTest.collectFromRecommendationMetadata(recommendationMetadata)

    then:
    result.isEmpty()
  }

  def 'should not collect metadata from recommendation when list contains multiple elements'() {
    given:
    def recommendationMetadata = new RecommendationMetadata()
    recommendationMetadata.setMatchesMetadata([new MatchMetadata(), new MatchMetadata()])

    when:
    def result = underTest.collectFromRecommendationMetadata(recommendationMetadata)

    then:
    result.isEmpty()
  }

  def 'should collect metadata from recommendation metadata'() {
    given:
    def recommendationMetadata = new RecommendationMetadata()
    recommendationMetadata.setMatchesMetadata([fixtures.matchMetadata])

    when:
    def result = underTest.collectFromRecommendationMetadata(recommendationMetadata)

    then:
    result.size() == 7
    with (result.first()) {
      key == 'feature_vector_signature'
      value == 'fv-1'
    }
    with (result[1]) {
      key == 'policy'
      value == 'policy1'
    }
    with (result[2]) {
      key == 'step'
      value == 'step1'
    }
    with (result[3]) {
      key == 'features/name:config'
      value == 'agents/name/versions/1.0.0/configs/1'
    }
    with (result[4]) {
      key == 'features/name:solution'
      value == 'EXACT_MATCH'
    }
    with (result[5]) {
      key == 'features/unknown:config'
      value == 'agents/unknown/versions/1.0.0/configs/1'
    }
    with (result[6]) {
      key == 'features/unknown:solution'
      value == 'EXACT_MATCH'
    }
  }

  class Fixtures {

    def matchMetadata = createMatchMetadata()

    def createMatchMetadata() {
      new MatchMetadata(
          match: 'alerts/1/match/1',
          solution: 'SOLUTION_FALSE_POSITIVE',
          reason: [
              'feature_vector_signature': 'fv-1',
              'policy': 'policy1',
              'step': 'step1'
          ] as Map,
          features: [
              'features/name': new FeatureMetadata(
                  agentConfig: 'agents/name/versions/1.0.0/configs/1',
                  solution: 'EXACT_MATCH',
                  reason: [] as Map
              ),
              'features/unknown': new FeatureMetadata(
                  agentConfig: 'agents/unknown/versions/1.0.0/configs/1',
                  solution: 'EXACT_MATCH',
                  reason: [] as Map
              )
          ]
      )
    }
  }
}
