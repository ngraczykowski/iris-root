package com.silenteight.connector.ftcc.callback.response.domain;

import java.util.List;

public interface MessageQuery {

  List<MessageEntity> findByBatchId(String batchId);
}
