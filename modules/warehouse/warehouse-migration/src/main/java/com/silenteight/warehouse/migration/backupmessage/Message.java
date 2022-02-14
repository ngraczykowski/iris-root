package com.silenteight.warehouse.migration.backupmessage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Data
@AllArgsConstructor
class Message {

  Long id;
  byte[] data;
  boolean migrated;
  OffsetDateTime migratedAt;

  void markMigrated() {
    setMigrated(true);
    setMigratedAt(OffsetDateTime.now(ZoneId.systemDefault()));
  }

  void markFailed() {
    setMigrated(false);
    setMigratedAt(OffsetDateTime.now(ZoneId.systemDefault()));
  }
}
