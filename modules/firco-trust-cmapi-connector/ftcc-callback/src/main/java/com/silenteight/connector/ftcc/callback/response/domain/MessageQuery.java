package com.silenteight.connector.ftcc.callback.response.domain;

import java.util.List;
import java.util.UUID;

public interface MessageQuery {

  List<MessageEntity> findByBatchId(UUID batchId);
}
