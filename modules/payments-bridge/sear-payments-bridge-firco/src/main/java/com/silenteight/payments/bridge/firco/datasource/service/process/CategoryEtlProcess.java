package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.payments.bridge.event.AlertRegistered;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;

import org.springframework.stereotype.Component;

@Component
class CategoryEtlProcess implements EtlProcess {

  @Override
  public void extractAndLoad(AlertRegistered data) {
      // send data to service
  }

  @Override
  public boolean supports(AlertRegistered data) {
    // check whether etl-process should handle the received data
    return false;
  }
}
