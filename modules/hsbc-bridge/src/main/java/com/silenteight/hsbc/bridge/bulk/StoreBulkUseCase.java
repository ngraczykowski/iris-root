package com.silenteight.hsbc.bridge.bulk;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent;
import com.silenteight.hsbc.bridge.bulk.exception.BatchWithGivenIdAlreadyCreatedException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@RequiredArgsConstructor
@Slf4j
class StoreBulkUseCase {

  private final AlertFacade alertFacade;
  private final BulkRepository bulkRepository;
  private final ApplicationEventPublisher eventPublisher;

  void handle(@NonNull StoreBulkUseCaseCommand command) {
    var bulkId = command.getBulkId();
    validateBulkId(bulkId);

    storeBulkWithAlerts(bulkId, command.isLearning(), command.getInputStream());
  }

  @Transactional
  public void storeBulkWithAlerts(String bulkId, boolean learning, InputStream inputStream) {
    var bulk = new Bulk(bulkId, learning);
    bulkRepository.save(bulk);
    tryToCreateAlerts(inputStream, bulk);

    log.info("Batch has been stored, ID: {}", bulkId);
  }

  private void tryToCreateAlerts(InputStream inputStream, Bulk bulk) {
    var bulkId = bulk.getId();
    try {
      alertFacade.createRawAlerts(bulkId, inputStream);
      eventPublisher.publishEvent(new BulkStoredEvent(bulkId));
    } catch (Exception e) {
      log.error("Cannot create alert data json, batchId = {}", bulkId, e);
      bulk.error("Enable to create alerts, due to: " + e.getMessage());
      bulkRepository.save(bulk);
    }
  }

  private void validateBulkId(String bulkId) {
    if (bulkRepository.existsById(bulkId)) {
      throw new BatchWithGivenIdAlreadyCreatedException(bulkId);
    }
  }

  @Builder
  @Value
  static class StoreBulkUseCaseCommand {

    @NonNull String bulkId;
    @NonNull InputStream inputStream;
    boolean learning;
  }
}
