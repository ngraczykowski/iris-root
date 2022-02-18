package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;

import java.util.UUID;

public interface AlertMessagePayloadUseCase {

  AlertMessageDto findByAlertMessageId(UUID alertMessageId);

}
