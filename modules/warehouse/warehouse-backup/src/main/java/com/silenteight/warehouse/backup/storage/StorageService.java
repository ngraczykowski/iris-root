package com.silenteight.warehouse.backup.storage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.backup.indexing.ProductionDataIndexStorable;
import com.silenteight.warehouse.backup.storage.BackupMessage.BackupMessageBuilder;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.util.JsonFormat;

@Slf4j
@RequiredArgsConstructor
public class StorageService implements ProductionDataIndexStorable {

  @NonNull
  private final BackupMessageRepository backupMessageRepository;

  private final boolean diagnosticEnabled;

  @Override
  public void save(AbstractMessage message, String requestId, String analysisName) {
    backupMessageRepository.save(buildMessage(message, requestId, analysisName));
  }

  @SneakyThrows
  private BackupMessage buildMessage(
      AbstractMessage message, String requestId, String analysisName) {

    BackupMessageBuilder messageBuilder = BackupMessage.builder()
        .requestId(requestId)
        .analysisName(analysisName)
        .data(message.toByteArray())
        .migrated(true);

    if (diagnosticEnabled)
      messageBuilder.diagnostic(JsonFormat.printer().sortingMapKeys().print(message));

    return messageBuilder.build();
  }
}
