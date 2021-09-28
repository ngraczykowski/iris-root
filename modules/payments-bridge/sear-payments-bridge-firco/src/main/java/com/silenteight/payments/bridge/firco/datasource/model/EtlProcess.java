package com.silenteight.payments.bridge.firco.datasource.model;

import com.silenteight.payments.bridge.event.AlertRegistered;

public interface EtlProcess {

  void extractAndLoad(AlertRegistered command);

  boolean supports(AlertRegistered command);

}
