package com.silenteight.connector.ftcc.app

import com.silenteight.connector.ftcc.app.CallbackConfiguration.CoreBridgeListener
import com.silenteight.connector.ftcc.app.IngestConfiguration.DataPrepListener
import com.silenteight.connector.ftcc.common.testing.BaseSpecificationIT
import com.silenteight.proto.fab.api.v1.AlertMessageStored
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State
import com.silenteight.proto.recommendation.api.v1.RecommendationsDelivered
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted
import com.silenteight.recommendation.api.library.v1.AlertOut
import com.silenteight.recommendation.api.library.v1.RecommendationOut
import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient
import com.silenteight.recommendation.api.library.v1.RecommendationsOut
import com.silenteight.registration.api.library.v1.RegisterBatchIn
import com.silenteight.registration.api.library.v1.RegistrationServiceClient

import com.github.tomakehurst.wiremock.WireMockServer
import com.google.common.io.Resources
import groovy.json.JsonSlurper
import org.spockframework.spring.SpringBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.util.concurrent.PollingConditions

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.silenteight.connector.ftcc.app.Fixtures.*
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals
import static org.skyscreamer.jsonassert.JSONCompareMode.LENIENT
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = FtccApplication)
@ActiveProfiles("dev")
@Import([IngestConfiguration, CallbackConfiguration])
class ConnectorIT extends BaseSpecificationIT {

  JsonSlurper jsonSlurper = new JsonSlurper()

  @LocalServerPort
  int port

  @Value('${server.servlet.context-path}')
  String contextPath

  @SpringBean
  RegistrationServiceClient registrationServiceClient = Mock()

  @Autowired
  DataPrepListener dataPrepListener

  @Autowired
  private RabbitTemplate rabbitTemplate

  @Value('${ftcc.core-bridge.inbound.batch-completed.exchange}')
  String queueName

  @Autowired
  CoreBridgeListener coreBridgeListener

  @Autowired
  RestTemplate restTemplate

  @SpringBean
  RecommendationServiceClient recommendationServiceClient = Mock()

  @Shared
  static WireMockServer WIRE_MOCK_SERVER = new WireMockServer(0)

  def setupSpec() {
    startPostgresql()
    startRabbitmq()
    WIRE_MOCK_SERVER.start()

    WIRE_MOCK_SERVER.stubFor(
        post(urlPathEqualTo("/rest/ftcc/callback"))
            .withRequestBody(equalToJson(CALLBACK_REQUEST))
            .willReturn(okJson(CALLBACK_RESPONSE)))
  }

  def cleanupSpec() {
    WIRE_MOCK_SERVER.stop()
    stopPostgresql()
    stopRabbitmq()
  }

  def cleanup() {
    dataPrepListener.clear()
    coreBridgeListener.clear()
  }

  @DynamicPropertySource
  private static void overrideCallbackEndpointPort(DynamicPropertyRegistry registry) {
    registry.add(
        "ftcc.cmapi.callback.endpoint",
        {"http://localhost:${WIRE_MOCK_SERVER.port()}/rest/ftcc/callback"})
  }

  def 'simple solving request'() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)
    String batchName
    String messageName = null

    when: "import alert"
    def request = new HttpEntity(Resources.getResource('solving-request.json').text)
    def response = restTemplate.postForEntity("http://localhost:$port$contextPath/v1/alert", request, String)

    then: "data-prep is notified"
    1 * registrationServiceClient.registerBatch(_) >> {RegisterBatchIn batch ->
      batchName = batch.getBatchId()
      assert batch.getBatchId() != null
      assert batch.getAlertCount() == 1
      assert !batch.getIsSimulation()
      assert batch.getBatchPriority() == 10
    }
    response.getStatusCode().value() == 200
    assertEquals(IMPORT_RESPONSE_JSON, response.getBody(), LENIENT)

    conditions.eventually {
      AlertMessageStored msg = dataPrepListener.getMessages().last()
      messageName = msg.getMessageName()
      assert msg.getBatchName() == batchName
      assert msg.getState() == State.NEW
      assert msg.getMessageName() != null
    }

    when: 'callback invoked'
    String batchId = batchName.split('/').last()
    MessageBatchCompleted batchCompleted = MessageBatchCompleted.newBuilder()
        .setBatchId(batchId)
        .setAnalysisName(ANALYSIS_NAME)
        .build()

    RecommendationsOut recommendationsOut = RecommendationsOut.builder()
        .recommendations(
            [RecommendationOut.builder()
                 .recommendationComment("Comment")
                 .recommendedAction("ACTION_POTENTIAL_TRUE_POSITIVE")
                 .alert(AlertOut.builder().id(messageName).build())
                 .build()])
        .build()

    1 * recommendationServiceClient.getRecommendations(RECOMMENDATIONS_IN) >> recommendationsOut
    rabbitTemplate.convertAndSend(queueName, 'solving', batchCompleted)

    then: "core-bridge is notified"
    noExceptionThrown()
    conditions.eventually {
      RecommendationsDelivered msg = coreBridgeListener.getMessages().last()
      assert msg.getBatchId() == batchId
      assert msg.getAnalysisName() == ANALYSIS_NAME
    }
  }

  def 'simple learning request'() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)
    String batchName
    String messageName = null

    when: "import alert"
    def request = new HttpEntity(Resources.getResource('learning-request.json').text)
    def response = restTemplate.postForEntity("http://localhost:$port$contextPath/v1/alert", request, String)

    then: "data-prep is notified"
    1 * registrationServiceClient.registerBatch(_) >> {RegisterBatchIn batch ->
      batchName = batch.getBatchId()
      assert batch.getBatchId() != null
      assert batch.getAlertCount() == 1
      assert batch.getIsSimulation()
      assert batch.getBatchPriority() == 1
    }
    response.getStatusCode().value() == 200
    assertEquals(IMPORT_RESPONSE_JSON, response.getBody(), LENIENT)

    conditions.eventually {
      AlertMessageStored msg = dataPrepListener.getMessages().last()
      messageName = msg.getMessageName()
      assert msg.getBatchName() == batchName
      assert msg.getState() == State.SOLVED_TRUE_POSITIVE
      assert msg.getMessageName() != null
    }
  }
}
