package com.silenteight.warehouse.backup.storage;

import org.springframework.data.repository.Repository;

interface BackupMessageRepository extends Repository<BackupMessage, Long> {

  BackupMessage save(BackupMessage reportEntity);

  BackupMessage findByRequestId(String requestId);
}

