package com.silenteight.hsbc.bridge.bulk;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent;
import com.silenteight.hsbc.bridge.bulk.exception.BatchAlertsLimitException;
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
    log.info("Batch has been stored, ID: {}, learning: {}", bulkId, learning);

    tryToCreateAlerts(inputStream, bulk);
    log.info("Raw alerts have been created, batchId: {}", bulkId);
  }

  private void tryToCreateAlerts(InputStream inputStream, Bulk bulk) {
    var bulkId = bulk.getId();
    try {
      log.info("Creating raw alerts, batchId: {}", bulkId);
      alertFacade.createRawAlerts(bulkId, inputStream);
      eventPublisher.publishEvent(new BulkStoredEvent(bulkId, bulk.isLearning()));
    } catch (BatchAlertsLimitException e) {
      setBulkError(bulkId, e);
      throw e;
    } catch (Exception e) {
      setBulkError(bulkId, e);
    }
  }

  private void validateBulkId(String bulkId) {
    if (bulkRepository.existsById(bulkId)) {
      throw new BatchWithGivenIdAlreadyCreatedException(bulkId);
    }
  }

  private void setBulkError(String bulkId, Throwable e) {
    log.error("Cannot create alert data json, batchId = {}", bulkId, e);
    bulkRepository.findById(bulkId).ifPresent(bulk -> {
      bulk.error("Unable to create alerts, due to: " + e.getMessage());
      bulkRepository.save(bulk);
    });
  }

  @Builder
  @Value
  static class StoreBulkUseCaseCommand {

    @NonNull String bulkId;
    @NonNull InputStream inputStream;
    boolean learning;
  }
}
