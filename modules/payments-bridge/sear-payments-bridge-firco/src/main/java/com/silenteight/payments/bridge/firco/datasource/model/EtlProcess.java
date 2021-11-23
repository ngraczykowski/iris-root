package com.silenteight.payments.bridge.firco.datasource.model;


import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;

public interface EtlProcess {

  void extractAndLoad(AeAlert alert, AlertEtlResponse alertEtlResponse);

}
