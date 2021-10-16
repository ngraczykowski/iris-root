package com.silenteight.payments.bridge.svb.oldetl.port;

import com.silenteight.payments.bridge.svb.oldetl.model.ExtractAlertedPartyDataRequest;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

public interface ExtractAlertedPartyDataUseCase {

  AlertedPartyData extractAlertedPartyData(ExtractAlertedPartyDataRequest request);
}
