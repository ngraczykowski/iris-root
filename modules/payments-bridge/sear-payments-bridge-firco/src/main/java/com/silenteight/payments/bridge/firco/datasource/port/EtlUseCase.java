package com.silenteight.payments.bridge.firco.datasource.port;

import com.silenteight.payments.bridge.common.model.AeAlert;

public interface EtlUseCase {

  void process(AeAlert alert);

}
