package com.silenteight.connector.ftcc.callback.response

import com.silenteight.connector.ftcc.callback.exception.NonRecoverableCallbackException
import com.silenteight.connector.ftcc.common.resource.BatchResource
import com.silenteight.connector.ftcc.common.resource.MessageResource
import com.silenteight.connector.ftcc.request.details.MessageDetailsQuery
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto

import spock.lang.Specification

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class MessageDetailsServiceTest extends Specification {

  def "Pass batches/[UUID] should return MessagesMap"() {
    given:
    def batchId = UUID.randomUUID()
    def batchName = BatchResource.toResourceName(batchId)
    def messageId = UUID.randomUUID()
    def service = init(batchId, messageId)

    when:
    def messagesMap = service.messages(batchName)

    then:
    !messagesMap.isEmpty()
    messagesMap.get(messageId).getBatchId() == batchId
    service.messageFrom(messagesMap, MessageResource.toResourceName(messageId)).getBatchId() ==
        batchId
  }

  def "Pass wrong batchName should throw IllegalArgumentException"() {
    given:
    def batchId = UUID.randomUUID()
    def messageId = UUID.randomUUID()
    def service = init(batchId, messageId)

    when:
    service.messages("batches/wrong_name")

    then:
    thrown(IllegalArgumentException)
  }

  def "Get Message with randomMessageName service should throw NonRecoverableCallbackException"() {
    given:
    def batchId = UUID.randomUUID()
    def batchName = BatchResource.toResourceName(batchId)
    def messageId = UUID.randomUUID()
    def randomMessageName = MessageResource.toResourceName(UUID.randomUUID())
    def service = init(batchId, messageId)
    def messagesMap = service.messages(batchName)

    when:
    service.messageFrom(messagesMap, randomMessageName).getBatchId()

    then:
    thrown(NonRecoverableCallbackException)
  }


  static MessageDetailsServiceImpl init(UUID batchId, UUID messageId) {
    def mock = mock(MessageDetailsQuery.class)
    def messageDtos = List.of(
        MessageDetailsDto.builder()
            .id(messageId)
            .batchId(batchId)
            .build())

    when(mock.details(batchId)).thenReturn(messageDtos)
    return new MessageDetailsServiceImpl(mock)
  }
}
