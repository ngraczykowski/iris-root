package com.silenteight.payments.bridge.svb.etl.port;

import com.silenteight.payments.bridge.svb.etl.model.ExtractAlertedPartyDataRequest;
import com.silenteight.payments.bridge.svb.etl.response.AlertedPartyData;

public interface ExtractAlertedPartyDataUseCase {

  AlertedPartyData extractAlertedPartyData(ExtractAlertedPartyDataRequest request);
}
