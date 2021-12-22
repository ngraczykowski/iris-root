package com.silenteight.warehouse.migration.backupmessage;

import lombok.Value;

@Value
class Message {

  Long id;
  byte[] data;
  boolean migrated;

  Message markMigrated() {
    return new Message(id, data, true);
  }
}
