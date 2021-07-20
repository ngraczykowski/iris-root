package com.silenteight.warehouse.backup.storage;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

public class InMemoryMessageRepository
    extends BasicInMemoryRepository<BackupMessage>
    implements MessageRepository {

  BackupMessage findOne() {
    return stream()
        .findFirst()
        .orElse(null);
  }
}
