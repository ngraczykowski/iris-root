package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;

import java.util.UUID;

public interface AlertMessagePayloadUseCase {

  AlertMessageDto findByAlertMessageId(UUID alertMessageId);

}
