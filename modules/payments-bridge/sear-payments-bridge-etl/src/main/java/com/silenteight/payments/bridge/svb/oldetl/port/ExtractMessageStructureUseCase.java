package com.silenteight.payments.bridge.svb.oldetl.port;

import com.silenteight.payments.bridge.svb.oldetl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.oldetl.model.ExtractAlertedPartyDataRequest;

public interface ExtractMessageStructureUseCase {

  AbstractMessageStructure extractMessageStructure(ExtractAlertedPartyDataRequest request);
}
