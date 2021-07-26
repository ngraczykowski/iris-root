package com.silenteight.warehouse.backup.storage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.warehouse.backup.indexing.ProductionDataIndexStorable;

@Slf4j
@RequiredArgsConstructor
public class StorageService implements ProductionDataIndexStorable {

  @NonNull
  private final BackupMessageRepository backupMessageRepository;

  @Override
  public void save(ProductionDataIndexRequest request) {
    backupMessageRepository.save(buildMessage(request));
  }

  private static BackupMessage buildMessage(ProductionDataIndexRequest request) {
    return BackupMessage.builder()
        .requestId(request.getRequestId())
        .analysisName(request.getAnalysisName())
        .data(request.toByteArray())
        .build();
  }
}