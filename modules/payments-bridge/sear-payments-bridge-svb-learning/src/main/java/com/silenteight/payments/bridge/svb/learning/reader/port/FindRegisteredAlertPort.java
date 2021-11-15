package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.svb.learning.reader.domain.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.RegisteredAlert;

import java.util.List;

public interface FindRegisteredAlertPort {

  List<RegisteredAlert> find(List<FindRegisteredAlertRequest> registeredAlert);

}
