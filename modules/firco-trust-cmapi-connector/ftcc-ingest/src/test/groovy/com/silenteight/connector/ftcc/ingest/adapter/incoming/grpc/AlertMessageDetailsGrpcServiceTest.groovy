package com.silenteight.connector.ftcc.ingest.adapter.incoming.grpc

import com.silenteight.connector.ftcc.request.domain.exception.MessageNotFoundException
import com.silenteight.connector.ftcc.request.get.MessageByIdsQuery
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsResponse

import io.grpc.internal.testing.StreamRecorder
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static com.silenteight.connector.ftcc.ingest.adapter.incoming.grpc.AlertMessageFixtures.*

@ContextConfiguration(classes = GrpcIncomingConfiguration)
class AlertMessageDetailsGrpcServiceTest extends Specification {

  @Autowired
  AlertMessageDetailsGrpcService underTest

  @SpringBean
  MessageByIdsQuery messageByIdsQuery = Mock()

  def "message details should be returned"() {
    given:
    StreamRecorder<AlertMessagesDetailsResponse> responseObserver = StreamRecorder.create()

    when:
    underTest.alertsDetails(ALERT_MESSAGES_DETAILS_REQUEST, responseObserver)
    responseObserver.awaitCompletion()

    then:
    1 * messageByIdsQuery.get(BATCH_ID, MESSAGE_ID) >> MESSAGE_DTO
    responseObserver.getError() == null
    responseObserver.getValues() == [ALERT_MESSAGES_DETAILS_RESPONSE]
  }

  def "error should be returned if message not exists"() {
    given:
    StreamRecorder<AlertMessagesDetailsResponse> responseObserver = StreamRecorder.create()

    when:
    underTest.alertsDetails(ALERT_MESSAGES_DETAILS_REQUEST, responseObserver)
    responseObserver.awaitCompletion()

    then:
    1 * messageByIdsQuery.get(BATCH_ID, MESSAGE_ID) >> {
      throw new MessageNotFoundException(BATCH_ID, MESSAGE_ID)
    }
    responseObserver.getError() != null
    responseObserver.getValues().isEmpty()
  }
}
