package com.silenteight.payments.bridge.ae.alertregistration.port;

import java.util.List;
import java.util.UUID;

public interface RegisterMatchDataAccessPort {

  void save(UUID alertId, List<String> matchNames);
}
