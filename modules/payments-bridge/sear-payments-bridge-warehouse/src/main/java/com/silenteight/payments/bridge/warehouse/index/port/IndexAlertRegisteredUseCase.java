package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.common.model.AlertData;

public interface IndexAlertRegisteredUseCase {

  void index(AlertData alertData, AlertMessageDto alertMessageDto, AeAlert aeAlert, String status);

}
