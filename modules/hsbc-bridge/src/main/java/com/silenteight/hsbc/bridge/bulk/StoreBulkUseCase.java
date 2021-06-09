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

  @Transactional
  void handle(@NonNull StoreBulkUseCaseCommand command) {
    var bulkId = command.getBulkId();
    validateBulkId(bulkId);

    var bulk = new Bulk(bulkId, command.isLearning());
    bulkRepository.save(bulk);

    alertFacade.createRawAlerts(command.getBulkId(), command.getInputStream());
    log.info("Batch has been stored, ID: {}", bulkId);

    eventPublisher.publishEvent(new BulkStoredEvent(bulkId));
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
