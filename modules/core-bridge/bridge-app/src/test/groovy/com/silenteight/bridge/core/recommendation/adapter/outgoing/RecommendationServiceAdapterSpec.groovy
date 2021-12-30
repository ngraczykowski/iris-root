package com.silenteight.bridge.core.recommendation.adapter.outgoing

import com.silenteight.adjudication.api.library.v1.recommendation.FeatureMetadataOut
import com.silenteight.adjudication.api.library.v1.recommendation.MatchMetadataOut
import com.silenteight.adjudication.api.library.v1.recommendation.RecommendationMetadataOut
import com.silenteight.adjudication.api.library.v1.recommendation.RecommendationServiceClient
import com.silenteight.adjudication.api.library.v1.recommendation.RecommendationWithMetadataOut

import spock.lang.Specification
import spock.lang.Subject

import java.time.OffsetDateTime

class RecommendationServiceAdapterSpec extends Specification {

  def recommendationClient = Mock(RecommendationServiceClient)

  @Subject
  def underTest = new RecommendationServiceAdapter(recommendationClient)

  def "should get and map recommendations"() {
    given:
    def testAnalysisName = "someAnalysis"

    and:
    recommendationClient.getRecommendations(testAnalysisName) >> [RECOMMENDATION_WITH_METADATA_OUT]

    when:
    def recommendationCollection = underTest.getRecommendations(testAnalysisName)

    then:
    recommendationCollection.size() == 1
    def recommendationWithMetadata = recommendationCollection.first()
    with(recommendationWithMetadata) {
      alertName() == "someAlert"
      analysisName() == testAnalysisName
      recommendedAt() == OffsetDateTime.MAX
      recommendedAction() == "someRecommendationAction"

      def matchMetadataList = metadata().matchMetadata()
      matchMetadataList.size() == 1

      with(matchMetadataList.first()) {
        match() == "someMatch"
        reason() == [reason: "someReason"]

        def feature = features().get("feature")
        with(feature) {
          agentConfig() == "someAgentConfig"
          solution() == "someSolution"
          reason() == [reason: "someReason"]
        }
      }
    }
  }

  private static FEATURE_METADATA_OUT = new FeatureMetadataOut(
      agentConfig: "someAgentConfig",
      solution: "someSolution",
      reason: [reason: "someReason"]
  )

  private static RECOMMENDATION_METADATA_OUT = new RecommendationMetadataOut(
      matchesMetadata: [new MatchMetadataOut(
          match: "someMatch",
          solution: "someSolution",
          reason: [reason: "someReason"],
          categories: [category: "someCategory"],
          features: [feature: FEATURE_METADATA_OUT]
      )]
  )

  private static RECOMMENDATION_WITH_METADATA_OUT = RecommendationWithMetadataOut.builder()
      .alert("someAlert")
      .name("someRecommendationName")
      .recommendedAction("someRecommendationAction")
      .recommendationComment("someRecommendationComment")
      .s8recommendedAction("someS8recommendationAction")
      .date(OffsetDateTime.MAX)
      .metadata(RECOMMENDATION_METADATA_OUT)
      .build()
}
