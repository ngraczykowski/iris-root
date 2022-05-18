package com.silenteight.payments.bridge.ae.alertregistration.port;

import java.util.List;

public interface DeleteRegisteredAlertUseCase {

  List<String> delete(List<String> alertNames);

}
