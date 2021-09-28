package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.event.AlertDelivered;
import com.silenteight.payments.bridge.event.AlertRegistered;

import java.util.function.Function;

public interface RegisterSingleAlertUseCase extends Function<AlertDelivered, AlertRegistered> {
}
