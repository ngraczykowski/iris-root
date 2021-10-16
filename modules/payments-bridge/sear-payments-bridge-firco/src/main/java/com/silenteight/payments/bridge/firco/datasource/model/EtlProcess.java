package com.silenteight.payments.bridge.firco.datasource.model;


import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;

public interface EtlProcess {

  void extractAndLoad(AlertRegisteredEvent command, AlertEtlResponse alertEtlResponse);

  boolean supports(AlertRegisteredEvent command);

}
