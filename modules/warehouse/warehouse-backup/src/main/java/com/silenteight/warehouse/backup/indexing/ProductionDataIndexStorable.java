package com.silenteight.warehouse.backup.indexing;

import com.google.protobuf.AbstractMessage;

public interface ProductionDataIndexStorable {

  void save(AbstractMessage message, String requestId, String analysisName);
}
