package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;

import com.silenteight.connector.ftcc.ingest.domain.dto.MessageDetailsDto;

import java.util.UUID;

public interface MessageDetailsQuery {

  MessageDetailsDto details(@NonNull UUID batchId, @NonNull UUID messageId);
}
