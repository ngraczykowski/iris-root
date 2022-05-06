package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.AlertMessage;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.connector.ftcc.ingest.state.AlertStateEvaluator;
import com.silenteight.connector.ftcc.request.store.RequestStorage;
import com.silenteight.connector.ftcc.request.store.dto.RequestStoreDto;
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.NEW;
import static java.lang.Boolean.FALSE;

@RequiredArgsConstructor
@Slf4j
public class IngestFacade {

  static final int SOLVING_PRIORITY = 8;
  static final int LEARNING_PRIORITY = 4;

  @NonNull
  private final RequestStorage requestStorage;
  @NonNull
  private final RegistrationApiClient registrationApiClient;
  @NonNull
  private final AlertStateEvaluator alertStateEvaluator;
  @NonNull
  private final DataPrepMessageGateway dataPrepMessageGateway;

  public void ingest(@NonNull @Valid RequestDto request, UUID batchId) {
    RequestStoreDto requestStore = requestStorage.store(request, batchId);
    registerBatch(request, batchId, requestStore.getMessageIds());
    sendToDataPrep(batchId, requestStore.getMessageIds());
  }

  private void registerBatch(RequestDto request, UUID batchId, List<UUID> messageIds) {
    String batchName = BatchResource.toResourceName(batchId);
    boolean isSolving = isSolvingBatch(batchId, messageIds);
    Batch batch = Batch.builder()
        .batchId(batchName)
        .alertsCount(request.getMessagesCount())
        .isSimulation(!isSolving)
        .build();
    registrationApiClient.registerBatch(batch);
    log.info("Registered batch={}", batch);
  }

  private void sendToDataPrep(UUID batchId, List<UUID> messageIds) {
    log.info("Preparation of data for sending to DataPrep: batchId={}", batchId);
    messageIds
        .stream()
        .map(messageId -> toAlertMessage(batchId, messageId))
        .peek(this::logAlertMessageStored)
        .forEach(dataPrepMessageGateway::send);
    log.info("Data has been sent to DataPrep: batchId={}", batchId);
  }

  private void logAlertMessageStored(AlertMessage alertMessage) {
    log.debug("Sending data to DataPrep: {}", alertMessage);
  }

  private AlertMessage toAlertMessage(UUID batchId, UUID messageId) {
    State state = evaluateAlertState(batchId, messageId);
    return AlertMessage.builder()
        .batchName(BatchResource.toResourceName(batchId))
        .messageName(MessageResource.toResourceName(messageId))
        .state(state)
        .priority(evaluatePriority(state))
        .build();
  }

  private State evaluateAlertState(UUID batchId, UUID messageId) {
    log.debug("Evaluating state for message: batchId={}, messageId={}", batchId, messageId);
    return alertStateEvaluator.evaluate(batchId, messageId);
  }

  private static int evaluatePriority(State state) {
    return state == NEW ? SOLVING_PRIORITY : LEARNING_PRIORITY;
  }

  private boolean isSolvingBatch(UUID batchId, List<UUID> messageIds) {
    return messageIds.stream()
        .findFirst()
        .map(messageId -> evaluateAlertState(batchId, messageId) == NEW)
        .orElse(FALSE);
  }
}
