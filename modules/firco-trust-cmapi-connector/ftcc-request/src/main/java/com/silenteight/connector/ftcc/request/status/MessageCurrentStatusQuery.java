package com.silenteight.connector.ftcc.request.status;

import lombok.NonNull;

import java.util.UUID;

public interface MessageCurrentStatusQuery {

  String currentStatus(@NonNull UUID batchId, @NonNull UUID messageId);
}
