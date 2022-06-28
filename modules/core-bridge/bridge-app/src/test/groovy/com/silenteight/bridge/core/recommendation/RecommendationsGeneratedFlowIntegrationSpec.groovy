package com.silenteight.bridge.core.recommendation

import com.silenteight.adjudication.api.v1.RecommendationsGenerated
import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.recommendation.adapter.incoming.Fixtures
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationIncomingRecommendationsGeneratedConfigurationProperties
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
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

  @Autowired
  private RabbitTemplate rabbitTemplate

  @Autowired
  private RecommendationRepository recommendationRepository

  @Autowired
  private BatchRepository batchRepository

  @Autowired
  private RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties

  def "should store recommendations and publish RecommendationsStored event when receive recommendations from AE"() {
    given:
    def analysisName = Fixtures.ANALYSIS_NAME
    def batch = Batch.builder()
        .id(UUID.randomUUID().toString())
        .analysisName(analysisName)
        .alertsCount(1)
        .status(BatchStatus.STORED)
        .batchPriority(0)
        .build()

    def recommendationsGenerated = RecommendationsGenerated.newBuilder()
        .setAnalysis(analysisName)
        .addAllRecommendationInfos([Fixtures.RECOMMENDATION_INFO])
        .build()
    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, factor: 1.25)

    batchRepository.create(batch)

    when:
    rabbitTemplate.convertAndSend(
        properties.exchangeName(),
        properties.exchangeRoutingKey(),
        recommendationsGenerated)

    then:
    conditions.eventually {
      def recommendations = recommendationRepository.findByAnalysisName(analysisName)
      assert recommendations.size() == 1

      def recommendationsStored = (RecommendationsStored) rabbitTemplate.receiveAndConvert(
          RecommendationsGeneratedFlowRabbitMqTestConfig.TEST_QUEUE_NAME, 10000L)
      with(recommendationsStored) {
        analysisName == analysisName
        alertNamesCount == 1
      }
    }
  }
}
