package com.silenteight.connector.ftcc.request.get;

import lombok.NonNull;

import com.silenteight.connector.ftcc.request.get.dto.MessageDto;

import java.util.UUID;

public interface MessageByIdsQuery {

  MessageDto get(@NonNull UUID batchId, @NonNull UUID messageId);
}
