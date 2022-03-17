package com.silenteight.scb.ingest.domain;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import java.util.List;

public interface BatchRegistrationService {

  void register(String batchId, List<Alert> alerts);
}
