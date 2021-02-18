package com.silenteight.hsbc.bridge.bulk.command;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class CancelBulkCommand {
  @NonNull UUID bulkId;
}
