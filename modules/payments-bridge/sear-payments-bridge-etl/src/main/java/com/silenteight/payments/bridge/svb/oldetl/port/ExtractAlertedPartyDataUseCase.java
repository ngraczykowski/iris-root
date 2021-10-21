package com.silenteight.payments.bridge.svb.oldetl.port;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

public interface ExtractAlertedPartyDataUseCase {

  AlertedPartyData extractAlertedPartyData(
      String applicationCode, MessageData messageData, String hitTag);
}
