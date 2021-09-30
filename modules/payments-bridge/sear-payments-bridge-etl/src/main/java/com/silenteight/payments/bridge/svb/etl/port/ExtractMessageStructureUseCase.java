package com.silenteight.payments.bridge.svb.etl.port;

import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.etl.model.ExtractAlertedPartyDataRequest;

public interface ExtractMessageStructureUseCase {

  AbstractMessageStructure extractMessageStructure(ExtractAlertedPartyDataRequest request);
}
