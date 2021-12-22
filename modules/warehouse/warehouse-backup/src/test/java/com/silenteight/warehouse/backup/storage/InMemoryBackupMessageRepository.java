package com.silenteight.warehouse.backup.storage;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

public class InMemoryBackupMessageRepository
    extends BasicInMemoryRepository<BackupMessage>
    implements BackupMessageRepository {

  BackupMessage findOne() {
    return stream()
        .findFirst()
        .orElse(null);
  }

  public BackupMessage findByRequestId(String requestId) {
    return stream()
        .filter(message -> message.getRequestId().equals(requestId))
        .findFirst()
        .orElse(null);
  }

}
