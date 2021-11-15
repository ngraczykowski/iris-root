package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.model.AlertData;

import java.util.List;

public interface RegisterAlertUseCase {

  RegisterAlertResponse register(AlertData alertData, AlertMessageDto alertMessageDto);

  List<RegisterAlertResponse> batchRegistration(List<RegisterAlertRequest> registerAlertRequest);

}
