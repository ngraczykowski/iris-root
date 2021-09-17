package com.silenteight.payments.bridge.firco.core.alertmessage.port;

import com.silenteight.payments.bridge.firco.dto.output.AlertDecisionMessageDto;

public interface ResponsePublisherPort {

  void send(AlertDecisionMessageDto decision);

}
