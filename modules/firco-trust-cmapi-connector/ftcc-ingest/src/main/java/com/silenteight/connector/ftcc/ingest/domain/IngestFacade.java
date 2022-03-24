package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class IngestFacade {

  @NonNull
  private final BatchIdGenerator batchIdGenerator;
  @NonNull
  private final RequestService requestService;
  @NonNull
  private final MessageService messageService;
  @NonNull
  private final RegistrationApiClient registrationApiClient;
  @NonNull
  private final DataPrepMessageGateway dataPrepMessageGateway;

  @Transactional
  public void ingest(@NonNull RequestDto request) {
    UUID batchId = batchIdGenerator.generate();
    List<UUID> messageIds = createRequestAndMessages(request, batchId);
    registerBatch(request, batchId);
    sendToDataPrep(batchId, messageIds);
  }

  private List<UUID> createRequestAndMessages(RequestDto request, UUID batchId) {
    requestService.create(batchId);
    return request
        .getMessages()
        .stream()
        .map(message -> messageService.create(batchId, message))
        .collect(toList());
  }

  private void registerBatch(RequestDto request, UUID batchId) {
    Batch batch = Batch.builder()
        .batchId(BatchResource.toResourceName(batchId))
        .alertsCount(request.getMessagesCount())
        .build();
    registrationApiClient.registerBatch(batch);
  }

  private void sendToDataPrep(UUID batchId, List<UUID> messageIds) {
    messageIds
        .stream()
        .map(messageId -> toAlertMessageStored(batchId, messageId))
        .forEach(dataPrepMessageGateway::send);
  }

  private AlertMessageStored toAlertMessageStored(UUID batchId, UUID messageId) {
    return AlertMessageStored.newBuilder()
        .setBatchName(BatchResource.toResourceName(batchId))
        .setMessageName(MessageResource.toResourceName(messageId))
        .build();
  }
}
