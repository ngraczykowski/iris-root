package com.silenteight.connector.ftcc.ingest.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.connector.ftcc.common.dto.input.RequestBodyDto;
import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.common.dto.input.RequestSendMessageDto;
import com.silenteight.connector.ftcc.request.store.dto.RequestStoreDto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class IngestFixtures {

  static final UUID BATCH_ID = fromString("558ecea2-a1d5-11eb-bcbc-0242ac130002");
  static final UUID MESSAGE_ID_1 = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
  static final UUID MESSAGE_ID_2 = fromString("f36a59e8-5793-4053-86b4-01dca35b9e63");
  static final UUID MESSAGE_ID_3 = fromString("980e1f4c-6c5b-45d2-8516-0998776a39c8");
  static final List<UUID> MESSAGE_IDS = List.of(MESSAGE_ID_1, MESSAGE_ID_2, MESSAGE_ID_3);
  static final RequestStoreDto REQUEST_STORE = RequestStoreDto.builder()
      .messageIds(MESSAGE_IDS)
      .build();

  static RequestDto makeRequestDto() {
    JsonNode message1 = new TextNode("message1");
    JsonNode message2 = new TextNode("message2");
    JsonNode message3 = new TextNode("message3");

    RequestSendMessageDto sendMessage = new RequestSendMessageDto();
    sendMessage.setMessages(List.of(message1, message2, message3));

    RequestBodyDto body = new RequestBodyDto();
    body.setSendMessageDto(sendMessage);

    RequestDto request = new RequestDto();
    request.setBody(body);

    return request;
  }
}
