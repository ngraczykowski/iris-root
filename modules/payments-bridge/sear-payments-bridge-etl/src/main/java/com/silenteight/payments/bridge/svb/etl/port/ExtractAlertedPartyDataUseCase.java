package com.silenteight.payments.bridge.svb.etl.port;

import com.silenteight.payments.bridge.svb.etl.model.AlertedPartyData;
import com.silenteight.payments.bridge.svb.etl.model.ExtractAlertedPartyDataRequest;

public interface ExtractAlertedPartyDataUseCase {

  AlertedPartyData extractAlertedPartyData(ExtractAlertedPartyDataRequest request);
}
