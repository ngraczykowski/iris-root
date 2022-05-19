package com.silenteight.bridge.core.recommendation

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationIncomingDataRetentionConfigurationProperties

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.util.concurrent.PollingConditions

import static com.silenteight.bridge.core.recommendation.RecommendationsDataRetentionFlowFixtures.*

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
@DirtiesContext
class RecommendationsDataRetentionFlowIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  RecommendationRepository recommendationRepository

  @Autowired
  RabbitTemplate rabbitTemplate

  @Autowired
  RecommendationIncomingDataRetentionConfigurationProperties properties

  @Autowired
  JdbcTemplate jdbcTemplate

  def 'should receive AlertsExpired and clear recommendation_comment and payload'() {
    given:
    recommendationRepository.saveAll(RECOMMENDATIONS)
    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, factor: 1.25)

    when:
    rabbitTemplate.convertAndSend(
        properties.exchangeName(), properties.alertsExpiredRoutingKey(), ALERTS_EXPIRED_MESSAGE)

    then:
    conditions.eventually {
      recommendationRepository.findByAnalysisName(ANALYSIS_NAME).each {
        assert it.recommendationComment() == null
        assert it.metadata() == null
      }
    }
  }
}
