package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.connector.ftcc.ingest.state.AlertStateEvaluator;
import com.silenteight.connector.ftcc.request.store.RequestStorage;
import com.silenteight.connector.ftcc.request.store.dto.RequestStoreDto;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class IngestFacade {

  @NonNull
  private final BatchIdGenerator batchIdGenerator;
  @NonNull
  private final RequestStorage requestStorage;
  @NonNull
  private final RegistrationApiClient registrationApiClient;
  @NonNull
  private final AlertStateEvaluator alertStateEvaluator;
  @NonNull
  private final DataPrepMessageGateway dataPrepMessageGateway;

  public void ingest(@NonNull RequestDto request) {
    UUID batchId = batchIdGenerator.generate();
    RequestStoreDto requestStore = requestStorage.store(request, batchId);
    registerBatch(request, batchId);
    sendToDataPrep(batchId, requestStore.getMessageIds());
  }

  private void registerBatch(RequestDto request, UUID batchId) {
    String batchName = BatchResource.toResourceName(batchId);
    Batch batch = Batch.builder()
        .batchId(batchName)
        .alertsCount(request.getMessagesCount())
        .build();
    registrationApiClient.registerBatch(batch);
    log.info("Registered batch={}", batchName);
  }

  private void sendToDataPrep(UUID batchId, List<UUID> messageIds) {
    log.info("Preparation of data for sending to DataPrep: batchId={}", batchId);
    messageIds
        .stream()
        .map(messageId -> toAlertMessageStored(batchId, messageId))
        .peek(this::logAlertMessageStored)
        .forEach(dataPrepMessageGateway::send);
    log.info("Data has been sent to DataPrep: batchId={}", batchId);
  }

  private void logAlertMessageStored(AlertMessageStored alertMessageStored) {
    if (log.isDebugEnabled())
      log.debug("Sending data to DataPrep: {}", alertMessageStored);
  }

  private AlertMessageStored toAlertMessageStored(UUID batchId, UUID messageId) {
    return AlertMessageStored.newBuilder()
        .setBatchName(BatchResource.toResourceName(batchId))
        .setMessageName(MessageResource.toResourceName(messageId))
        .setState(evaluateAlertState(batchId, messageId))
        .build();
  }

  private State evaluateAlertState(UUID batchId, UUID messageId) {
    if (log.isDebugEnabled())
      log.debug("Evaluating state for message: batchId={}, messageId={}", batchId, messageId);

    return alertStateEvaluator.evaluate(batchId, messageId);
  }
}
