package com.silenteight.adjudication.engine.solving.storage;

import com.silenteight.adjudication.engine.solving.domain.AlertSolvingModel;

public interface StorageProvider {

  void put(String key, AlertSolvingModel model);

  AlertSolvingModel get(String key);
}
