package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.Label;

import java.util.List;

public interface AddAlertLabelUseCase {

  void invoke(List<String> alertNames, List<Label> labels);
}
