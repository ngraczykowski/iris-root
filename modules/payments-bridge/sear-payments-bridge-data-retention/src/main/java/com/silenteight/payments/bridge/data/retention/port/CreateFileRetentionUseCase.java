package com.silenteight.payments.bridge.data.retention.port;

import com.silenteight.payments.bridge.data.retention.model.FileDataRetention;

public interface CreateFileRetentionUseCase {

  void create(Iterable<FileDataRetention> dataRetentions);
}
