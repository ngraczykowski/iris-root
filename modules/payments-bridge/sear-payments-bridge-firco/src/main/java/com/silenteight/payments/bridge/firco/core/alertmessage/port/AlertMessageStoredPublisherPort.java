package com.silenteight.payments.bridge.firco.core.alertmessage.port;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.FircoAlertMessage;

public interface AlertMessageStoredPublisherPort {

  boolean publish(FircoAlertMessage message);

}
