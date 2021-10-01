package com.silenteight.payments.bridge.firco.datasource.model;

import com.silenteight.payments.bridge.event.AlertRegistered;
import com.silenteight.payments.bridge.svb.etl.response.AlertEtlResponse;

public interface EtlProcess {

  void extractAndLoad(AlertRegistered command, AlertEtlResponse alertEtlResponse);

  boolean supports(AlertRegistered command);

}
