package com.silenteight.bridge.core.reports

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.reports.domain.port.outgoing.RecommendationService
import com.silenteight.bridge.core.reports.domain.port.outgoing.RegistrationService
import com.silenteight.bridge.core.reports.infrastructure.amqp.ReportsIncomingBatchDeliveredProperties
import com.silenteight.bridge.core.reports.infrastructure.amqp.ReportsOutgoingConfigurationProperties
import com.silenteight.data.api.v2.ProductionDataIndexRequest
import com.silenteight.proto.registration.api.v1.MessageBatchDelivered

import org.spockframework.spring.SpringBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.util.concurrent.PollingConditions

import java.util.stream.Stream

@ActiveProfiles("test")
@Import(ReportsSenderFlowRabbitMqTestConfig.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ReportsSenderFlowIntegrationSpec extends BaseSpecificationIT {

  @SpringBean
  RegistrationService registrationService = Stub {
    getAlertsWithMatches(ReportFixtures.BATCH_ID) >> [ReportFixtures.ALERT_ONE]
    streamAlerts(ReportFixtures.BATCH_ID) >> Stream.of(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES)
    getMatches(Set.of(1L)) >> List.of(ReportFixtures.MATCH_ONE_WITH_ALERT_ID)
  }

  @SpringBean
  RecommendationService recommendationService = Stub {
    getRecommendations(ReportFixtures.ANALYSIS_NAME) >> ReportFixtures.RECOMMENDATIONS_WITH_METADATA
    getRecommendations(ReportFixtures.ANALYSIS_NAME, List.of(ReportFixtures.ALERT_ONE_NAME)) >> ReportFixtures.RECOMMENDATIONS_WITH_METADATA
  }

  @Autowired
  ReportsIncomingBatchDeliveredProperties reportsIncomingBatchDeliveredProperties

  @Autowired
  ReportsOutgoingConfigurationProperties reportsOutgoingConfigurationProperties

  @Autowired
  RabbitTemplate rabbitTemplate

  def 'should send data to warehouse for batch delivered event'() {
    given:
    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, factor: 1.25)
    def batchDeliveredMessage = MessageBatchDelivered.newBuilder()
        .setBatchId(ReportFixtures.BATCH_ID)
        .setAnalysisName(ReportFixtures.ANALYSIS_NAME)
        .build()

    when:
    rabbitTemplate.convertAndSend(
        reportsIncomingBatchDeliveredProperties.exchangeName(),
        reportsIncomingBatchDeliveredProperties.exchangeRoutingKey(),
        batchDeliveredMessage
    )

    then:
    noExceptionThrown()
    conditions.eventually {
      def productionDataIndexRequest = (ProductionDataIndexRequest) rabbitTemplate
          .receiveAndConvert(
              ReportsSenderFlowRabbitMqTestConfig.TEST_QUEUE_NAME, 10000L)

      with(productionDataIndexRequest) {
        it.analysisName == ReportFixtures.ANALYSIS_NAME
        it.alertsList.containsAll(ReportFixtures.WAREHOUSE_ALERT)
      }
    }
  }
}
