package com.silenteight.connector.ftcc.request.store;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.request.domain.MessageService;
import com.silenteight.connector.ftcc.request.domain.RequestService;
import com.silenteight.connector.ftcc.request.store.dto.RequestStoreDto;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
public class RequestStorage {

  @NonNull
  private final RequestService requestService;
  @NonNull
  private final MessageService messageService;

  @Transactional
  public RequestStoreDto store(@NonNull @Valid RequestDto request, @NonNull UUID batchId) {
    log.info("Store request for batchId={}", batchId);
    requestService.create(batchId);
    List<UUID> messageIds = registerMessages(request.getMessages(), batchId);

    return RequestStoreDto.builder()
        .messageIds(messageIds)
        .build();
  }

  private List<UUID> registerMessages(List<JsonNode> messages, UUID batchId) {
    return messages
        .stream()
        .map(message -> messageService.create(batchId, message))
        .peek(
            messageUuid -> log.info(
                "Stored message request for batchId={} messageId={}", batchId, messageUuid))
        .collect(toList());
  }
}
