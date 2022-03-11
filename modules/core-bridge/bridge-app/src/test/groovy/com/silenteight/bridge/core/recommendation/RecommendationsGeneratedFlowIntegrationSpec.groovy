package com.silenteight.bridge.core.recommendation

import com.silenteight.adjudication.api.v1.RecommendationsGenerated
import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationIncomingRecommendationsGeneratedConfigurationProperties
import com.silenteight.proto.recommendation.api.v1.RecommendationsStored

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.util.concurrent.PollingConditions

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Import(RecommendationsGeneratedFlowRabbitMqTestConfig.class)
@ActiveProfiles("test")
@DirtiesContext
class RecommendationsGeneratedFlowIntegrationSpec extends BaseSpecificationIT {

  private static final String ANALYSIS_NAME = "ANALYSIS_NAME"

  @Autowired
  private RabbitTemplate rabbitTemplate

  @Autowired
  private RecommendationRepository recommendationRepository

  @Autowired
  private RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties

  def "should store recommendations and publish RecommendationsReceivedEvent event when receive recommendations from AE"() {
    given:
    def recommendationsGenerated = RecommendationsGenerated.newBuilder()
        .setAnalysis(ANALYSIS_NAME)
        .build()
    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, factor: 1.25)

    when:
    rabbitTemplate.convertAndSend(
        properties.exchangeName(),
        properties.exchangeRoutingKey(),
        recommendationsGenerated)

    then:
    conditions.eventually {
      def recommendations = recommendationRepository.findByAnalysisName(ANALYSIS_NAME)
      assert recommendations.size() == 2

      def recommendationsReceived = (RecommendationsStored) rabbitTemplate.receiveAndConvert(
          RecommendationsGeneratedFlowRabbitMqTestConfig.TEST_QUEUE_NAME, 10000L)
      with(recommendationsReceived) {
        analysisName == ANALYSIS_NAME
        alertNamesCount == 2
      }
    }
  }
}
