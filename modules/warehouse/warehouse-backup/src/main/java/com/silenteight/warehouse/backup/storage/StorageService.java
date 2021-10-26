package com.silenteight.warehouse.backup.storage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.warehouse.backup.indexing.ProductionDataIndexStorable;
import com.silenteight.warehouse.backup.storage.BackupMessage.BackupMessageBuilder;

import com.google.protobuf.util.JsonFormat;

@Slf4j
@RequiredArgsConstructor
public class StorageService implements ProductionDataIndexStorable {

  @NonNull
  private final BackupMessageRepository backupMessageRepository;

  private final boolean diagnosticEnabled;

  @Override
  public void save(ProductionDataIndexRequest request) {
    backupMessageRepository.save(buildMessage(request));
  }

  @SneakyThrows
  private BackupMessage buildMessage(ProductionDataIndexRequest request) {
    BackupMessageBuilder messageBuilder = BackupMessage.builder()
        .requestId(request.getRequestId())
        .analysisName(request.getAnalysisName())
        .data(request.toByteArray());

    if (diagnosticEnabled)
      messageBuilder.diagnostic(JsonFormat.printer().sortingMapKeys().print(request));

    return messageBuilder.build();
  }
}
