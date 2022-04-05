package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;

import java.util.Map;
import java.util.UUID;

import static java.util.Optional.of;

public interface MessageDetailsService {

  Map<UUID, MessageDetailsDto> messages(String batchName);

  default MessageDetailsDto messageFrom(Map<UUID, MessageDetailsDto> messages, String messageName) {
    var messageId = MessageResource.fromResourceName(messageName);
    return of(messages.get(messageId)).orElseThrow();
  }
}
