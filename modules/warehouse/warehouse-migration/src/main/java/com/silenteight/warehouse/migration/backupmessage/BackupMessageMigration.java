package com.silenteight.warehouse.migration.backupmessage;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.warehouse.production.handler.ProductionRequestV1CommandHandler;
import com.silenteight.warehouse.production.handler.ProductionRequestV2CommandHandler;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@RequiredArgsConstructor
class BackupMessageMigration {

  private static final String PROCESS_NAME = "warehouse_backup_migration";
  private static final int LOCK_TIMEOUT = 2;

  private final MigrationProperties migrationProperties;
  private final BackupMessageQuery backupMessageQuery;
  private final LockRegistry lockRegistry;
  private final RabbitMessageContainerLifecycle rabbitMessageContainerLifecycle;
  private final TransactionTemplate transactionTemplate;
  private final ProductionRequestV1CommandHandler productionRequestV1CommandHandler;
  private final ProductionRequestV2CommandHandler productionRequestV2CommandHandler;

  @SneakyThrows
  @EventListener(ApplicationStartedEvent.class)
  @Scheduled(cron = "0 0/${random.int[1,10]} * * * *")
  public void migration() {

    boolean notMigratedRecords = backupMessageQuery.notMigratedRecordExist();

    if (notMigratedRecords) {
      lockAndProcess();
    } else {
      startRabbitListeners();
    }

  }

  private void lockAndProcess() throws InterruptedException {
    boolean processingError = false;

    Lock lockKey = lockRegistry.obtain(PROCESS_NAME);
    boolean lockAcquired = lockKey.tryLock(LOCK_TIMEOUT, TimeUnit.SECONDS);

    if (lockAcquired) {
      log.debug("Starting warehouse_message_backup table migration");
      try {
        process();
      } catch (Exception ex) {
        log.warn("Migration warehouse_message_backup failed due to: {}", ex.getMessage());
        processingError = true;
      } finally {
        lockKey.unlock();
      }
      if (!processingError) {
        startRabbitListeners();
      }
    }

  }

  private void process() {
    Long elementsToProcess = backupMessageQuery.count();
    log.debug("warehouse_message_backup migration: elements to process: {}", elementsToProcess);

    List<Message> toMigrate =
        backupMessageQuery.findMigrationCandidates(migrationProperties.getBatchSize());

    while (!toMigrate.isEmpty()) {

      transactionTemplate.execute(
          new BatchProcessWithinTransaction(Collections.unmodifiableList(toMigrate)));

      elementsToProcess -= toMigrate.size();
      log.debug(
          "warehouse_message_backup migration: elements left: {}",
          Math.max(0, elementsToProcess));

      toMigrate = backupMessageQuery.findMigrationCandidates(migrationProperties.getBatchSize());
    }
  }

  @RequiredArgsConstructor
  private class BatchProcessWithinTransaction extends TransactionCallbackWithoutResult {

    private final List<Message> messages;

    @Override
    protected void doInTransactionWithoutResult(TransactionStatus status) {
      try {
        messages.stream()
            .map(Message::markMigrated)
            .forEach(message -> {
              backupMessageQuery.update(message);
              persist(message);
            });
      } catch (Exception ex) {
        log.warn(
            "warehouse_message_backup migration: "
                + "exception occurred during batch processing, transaction will be rollback : {}",
            ex.getMessage());

        status.setRollbackOnly();
      }
    }

    @SneakyThrows
    private void persist(Message message) {
      try {
        com.silenteight.data.api.v1.ProductionDataIndexRequest dataIndexRequestV1 =
            com.silenteight.data.api.v1.ProductionDataIndexRequest.parseFrom(message.getData());
        productionRequestV1CommandHandler.handle(dataIndexRequestV1);
      } catch (InvalidProtocolBufferException ex) {
        try {
          ProductionDataIndexRequest dataIndexRequestV2 =
              ProductionDataIndexRequest.parseFrom(message.getData());
          productionRequestV2CommandHandler.handle(dataIndexRequestV2);
        } catch (InvalidProtocolBufferException e) {
          throw new TypeNotPresentException(
              "warehouse_message_backup migration: unexpected backup message data, "
                  + "unable parse to V1/V2 ProductionDataIndexRequest", e.getCause());
        }
      }
    }

  }

  private void startRabbitListeners() {
    log.debug("Finished warehouse_message_backup table migration");
    rabbitMessageContainerLifecycle.startRabbitListeners();
  }

}
