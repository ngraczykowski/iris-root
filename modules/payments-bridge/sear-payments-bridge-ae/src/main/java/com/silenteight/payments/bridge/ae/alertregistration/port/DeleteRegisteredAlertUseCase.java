package com.silenteight.payments.bridge.ae.alertregistration.port;

import java.util.List;
import java.util.UUID;

public interface DeleteRegisteredAlertUseCase {

  List<UUID> delete(List<String> alertNames);

}
