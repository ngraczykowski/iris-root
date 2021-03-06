package com.silenteight.bridge.core.recommendation.domain.port.outgoing

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.recommendation.domain.model.FeatureMetadata
import com.silenteight.bridge.core.recommendation.domain.model.MatchMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext

import java.time.OffsetDateTime

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class RecommendationRepositoryIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private RecommendationRepository recommendationRepository

  def "should save and get recommendation from database"() {
    given:
    def recommendations = [Fixtures.RECOMMENDATION_WITH_METADATA]

    when:
    recommendationRepository.saveAll(recommendations)

    then:
    def recommendation = recommendationRepository.findByAnalysisName(Fixtures.ANALYSIS_NAME)
    recommendation.size() == 1
    with(recommendation.first()) {
      name() == "someRecommendationName"
      analysisName() == Fixtures.ANALYSIS_NAME
      metadata() == Fixtures.RECOMMENDATION_METADATA
    }
  }

  private static class Fixtures {

    static ANALYSIS_NAME = "analysisName"
    static RECOMMENDATION_DATE = "2021-12-30T10:09:37.631749580+01:00"

    private static FEATURES_METADATA = FeatureMetadata.builder()
        .agentConfig("someAgentConfig")
        .solution("someSolution")
        .reason([reason: "someReason"])
        .build()

    private static MATCH_METADATA = MatchMetadata.builder()
        .match("someMatchName")
        .solution("someSolution")
        .reason([reason: "someReason"])
        .categories([category: "someCategory"])
        .features([feature: FEATURES_METADATA])
        .build()

    static RECOMMENDATION_METADATA = new RecommendationMetadata([MATCH_METADATA])

    static RECOMMENDATION_WITH_METADATA = RecommendationWithMetadata.builder()
        .alertName("alert/1")
        .analysisName(ANALYSIS_NAME)
        .name("someRecommendationName")
        .recommendedAt(OffsetDateTime.parse(RECOMMENDATION_DATE))
        .recommendationComment("someComment")
        .recommendedAction("someAction")
        .metadata(RECOMMENDATION_METADATA)
        .build()
  }
}
