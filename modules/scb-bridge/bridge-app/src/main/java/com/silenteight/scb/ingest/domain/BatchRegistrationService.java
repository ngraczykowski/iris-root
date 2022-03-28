package com.silenteight.scb.ingest.domain;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.RegistrationAlertContext;

import java.util.List;

public interface BatchRegistrationService {

  void register(String internalBatchId,
      List<Alert> alerts,
      RegistrationAlertContext registrationAlertContext);
}
