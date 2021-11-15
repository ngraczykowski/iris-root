package com.silenteight.payments.bridge.firco.datasource.port;

import com.silenteight.payments.bridge.event.AlertRegisteredEvent;

public interface EtlUseCase {

  void process(AlertRegisteredEvent event);

}
