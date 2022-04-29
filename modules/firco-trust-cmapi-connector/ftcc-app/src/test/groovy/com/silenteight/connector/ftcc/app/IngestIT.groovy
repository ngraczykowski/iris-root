package com.silenteight.connector.ftcc.app

import com.silenteight.connector.ftcc.app.IngestConfiguration.DataPrepListener
import com.silenteight.connector.ftcc.common.testing.BaseSpecificationIT
import com.silenteight.proto.fab.api.v1.AlertMessageStored
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State
import com.silenteight.registration.api.library.v1.RegisterBatchIn
import com.silenteight.registration.api.library.v1.RegistrationServiceClient

import com.google.common.io.Resources
import groovy.json.JsonSlurper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions
import wslite.rest.RESTClient

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals
import static org.skyscreamer.jsonassert.JSONCompareMode.LENIENT
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = FtccApplication)
@ActiveProfiles("dev")
@Import(IngestConfiguration)
class IngestIT extends BaseSpecificationIT {

  JsonSlurper jsonSlurper = new JsonSlurper()

  @LocalServerPort
  int port

  @Value('${server.servlet.context-path}')
  String contextPath

  @SpringBean
  RegistrationServiceClient registrationServiceClient = Mock()

  @Autowired
  DataPrepListener dataPrepListener

  def setupSpec() {
    startPostgresql()
    startRabbitmq()
  }

  def cleanup() {
    dataPrepListener.clear()
  }

  @Unroll
  def 'simple solving request #request'() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)
    def batchName

    when:
    RESTClient client = new RESTClient("http://localhost:$port$contextPath")
    def response = client.post(path: "/v1/alert") {
      json(jsonSlurper.parseText(Resources.getResource(request).text))
    }

    then:
    1 * registrationServiceClient.registerBatch(_) >> {RegisterBatchIn batch ->
      batchName = batch.getBatchId()
      assert batch.getBatchId() != null
      assert batch.getAlertCount() == 1
      assert batch.getIsSimulation() == isSimulation
      assert batch.getBatchPriority() == batchPriority
    }
    response.getStatusCode() == 200
    assertEquals(
        '''{
  "Header": null,
  "Body": {
    "msg_Acknowledgement": {
      "faultcode": "0",
      "faultstring": "OK",
      "faultactor": "cmapi.send.message"
    }
  }
}''', response.getContentAsString(), LENIENT)

    conditions.eventually {
      AlertMessageStored msg = dataPrepListener.getMessages().last()
      assert msg.getBatchName() == batchName
      assert msg.getState() == alertState
      assert msg.getMessageName() != null
    }

    where:
    request                 | isSimulation | batchPriority | alertState
    'solving-request.json'  | false        | 10            | State.NEW
    'learning-request.json' | true         | 1             | State.SOLVED_TRUE_POSITIVE
  }
}
