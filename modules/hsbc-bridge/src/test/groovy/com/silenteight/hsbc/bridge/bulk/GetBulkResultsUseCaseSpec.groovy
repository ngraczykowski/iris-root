package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertStatus
import com.silenteight.hsbc.bridge.bulk.rest.AlertMetadata
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatus
import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase
import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase.GetRecommendationRequest
import com.silenteight.hsbc.bridge.recommendation.RecommendationWithMetadataDto
import com.silenteight.hsbc.bridge.recommendation.metadata.FeatureMetadata
import com.silenteight.hsbc.bridge.recommendation.metadata.MatchMetadata
import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata

import spock.lang.Specification

import java.time.OffsetDateTime

class GetBulkResultsUseCaseSpec extends Specification {

  def fixtures = new Fixtures()
  def recommendationFacade = Mock(GetRecommendationUseCase)
  def bulkRepository = Mock(BulkRepository)
  def underTest = new GetBulkResultsUseCase(bulkRepository, recommendationFacade)

  def 'should get bulk results and check metadata'() {
    given:
    def bulkId = fixtures.bulkId

    when:
    def result = underTest.getResults(bulkId)

    then:
    with(result) {
      bulkId == bulkId
      batchStatus == BatchStatus.COMPLETED

      def solvedAlertMetadata = result.alerts.first().getAlertMetadata().sort()
      def expectedAlertMetadata = [
          new AlertMetadata('key-1', 'value-1'),
          new AlertMetadata('recommendationYear', fixtures.expectedDateYear),
          new AlertMetadata('recommendationMonth', fixtures.expectedDateMonth),
          new AlertMetadata('recommendationDay', fixtures.expectedDateDay),
          new AlertMetadata('recommendationDate', fixtures.expectedDate),
          new AlertMetadata('s8_recommendation', 'someS8recommendedAction'),
          new AlertMetadata('feature_vector_signature', 'feature_vector_signature_value'),
          new AlertMetadata('policy', 'policies/some-value'),
          new AlertMetadata('step', 'steps/some-value'),
          new AlertMetadata('features/name:config', 'agents/name/versions/1.0.0/configs/1'),
          new AlertMetadata('features/name:solution', 'EXACT_MATCH'),
          new AlertMetadata('category_1', 'category_1_value'),
          new AlertMetadata('category_2', 'category_2_value'),
          new AlertMetadata('category_3', 'category_3_value'),
      ].sort()
      solvedAlertMetadata == expectedAlertMetadata

      with(alerts.last()){
        errorMessage == 'error message'
        def errorAlertMetadata = getAlertMetadata()
        errorAlertMetadata == [
            new AlertMetadata('key-1', 'value-1')
        ]
      }
    }

    1 * bulkRepository.findById(bulkId) >> Optional.of(fixtures.bulk)
    1 * recommendationFacade.getRecommendation(_ as GetRecommendationRequest) >>
        fixtures.recommendationDto
  }

  class Fixtures {

    def bulk = createBulk()
    def bulkId = bulk.id
    def recommendationDto = createRecommendationDto()
    def expectedDate = recommendationDto.getDate().toString()
    def expectedDateYear = recommendationDto.getDate().getYear().toString()
    def expectedDateMonth = recommendationDto.getDate().getMonthValue().toString()
    def expectedDateDay = recommendationDto.getDate().getDayOfMonth().toString()

    def createBulk() {
      def bulk = new Bulk(
          analysis: new BulkAnalysisEntity(
              policy: 'policyName'
          ),
          id: '20210101-1111',
          status: BulkStatus.COMPLETED,
          alerts: [
              new BulkAlertEntity(
                  id: 1L,
                  externalId: 'alert-1',
                  name: 'alerts/1',
                  status: AlertStatus.COMPLETED,
                  metadata: [
                      new BulkAlertMetadata(
                          key: "key-1",
                          value: "value-1"
                      )
                  ]
              ),
              new BulkAlertEntity(
                  id: 2L,
                  externalId: 'alert-2',
                  name: 'alerts/2',
                  errorMessage: 'error message',
                  status: AlertStatus.ERROR,
                  metadata: [
                      new BulkAlertMetadata(
                          key: "key-1",
                          value: "value-1"
                      )
                  ]
              )
          ]
      )
      bulk
    }

    def createRecommendationDto() {
      def dto = RecommendationWithMetadataDto.builder()
          .alert('alerts/1')
          .recommendationComment('S8 Recommendation: Manual Investigation')
          .recommendedAction('MANUAL_INVESTIGATION')
          .date(OffsetDateTime.now())
          .name('recommendations/recommendation-1')
          .metadata(createDtoMetadata())
          .s8recommendedAction("someS8recommendedAction")
          .build()
      dto
    }

    def createDtoMetadata() {
      var metadata = new RecommendationMetadata(
          matchesMetadata: List.of(createDtoMatchMetadata())
      )
      metadata
    }

    def createDtoMatchMetadata() {
      def dtoMetadata = new MatchMetadata(
          features: createFeaturesDto(),
          match: 'alerts/1/matches/1',
          solution: 'SOLUTION_FALSE_POSITIVE',
          reason: Map.of(
              "feature_vector_signature", "feature_vector_signature_value",
              "policy", "policies/some-value",
              "step", "steps/some-value"
          ),
          categories: Map.of(
              "category_1", "category_1_value",
              "category_2", "category_2_value",
              "category_3", "category_3_value"
          )
      )
      dtoMetadata
    }

    def createFeaturesDto() {
      def featuresDto = Map.of(
          "features/name", createFeatureMetadata()
      )
      featuresDto
    }

    def createFeatureMetadata() {
      def metadata = new FeatureMetadata(
          agentConfig: 'agents/name/versions/1.0.0/configs/1',
          solution: 'EXACT_MATCH',
          reason: Map.of(
              "name", "Some dummy reason"
          )
      )
      metadata
    }
  }
}
