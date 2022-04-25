package com.silenteight.connector.ftcc.ingest.adapter.incoming.grpc

import com.silenteight.connector.ftcc.common.resource.BatchResource
import com.silenteight.connector.ftcc.common.resource.MessageResource
import com.silenteight.connector.ftcc.request.get.dto.MessageDto
import com.silenteight.proto.fab.api.v1.AlertMessageDetails
import com.silenteight.proto.fab.api.v1.AlertMessageHeader
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsRequest
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsResponse

import lombok.AccessLevel
import lombok.NoArgsConstructor

import static java.util.UUID.randomUUID

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AlertMessageFixtures {

  def static BATCH_ID = randomUUID()
  def static MESSAGE_ID = randomUUID()
  def static BATCH_NAME = BatchResource.toResourceName(BATCH_ID)
  def static MESSAGE_NAME = MessageResource.toResourceName(MESSAGE_ID)
  def static PAYLOAD = '{"a": "1"}'

  static AlertMessagesDetailsRequest ALERT_MESSAGES_DETAILS_REQUEST = AlertMessagesDetailsRequest
      .newBuilder()
      .addAlerts(
          AlertMessageHeader.newBuilder()
              .setBatchName(BATCH_NAME)
              .setMessageName(MESSAGE_NAME)
              .build())
      .build()

  static MessageDto MESSAGE_DTO = MessageDto.builder()
      .batchName(BATCH_NAME)
      .messageName(MESSAGE_NAME)
      .payload(PAYLOAD)
      .build()

  static AlertMessagesDetailsResponse ALERT_MESSAGES_DETAILS_RESPONSE = AlertMessagesDetailsResponse
      .newBuilder()
      .addAlerts(
          AlertMessageDetails.newBuilder()
              .setMessageName(MESSAGE_NAME)
              .setPayload(PAYLOAD)
              .build())
      .build()
}
