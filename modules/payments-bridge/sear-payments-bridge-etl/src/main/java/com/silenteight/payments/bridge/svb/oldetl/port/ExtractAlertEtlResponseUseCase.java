package com.silenteight.payments.bridge.svb.oldetl.port;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;

public interface ExtractAlertEtlResponseUseCase {

  AlertEtlResponse createAlertEtlResponse(AlertMessageDto alertMessageDto);
}
