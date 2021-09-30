package com.silenteight.payments.bridge.svb.etl.port;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.svb.etl.response.AlertEtlResponse;

public interface ExtractAlertEtlResponseUseCase {

  AlertEtlResponse createAlertEtlResponse(AlertMessageDto alertMessageDto);
}
