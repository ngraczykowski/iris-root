package com.silenteight.warehouse.backup.storage;

import org.springframework.data.repository.Repository;

interface MessageRepository extends Repository<BackupMessage, Long> {

  BackupMessage save(BackupMessage reportEntity);
}

