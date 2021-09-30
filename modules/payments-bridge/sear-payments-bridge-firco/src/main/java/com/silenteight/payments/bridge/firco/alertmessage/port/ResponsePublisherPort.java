package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.common.dto.output.AlertDecisionMessageDto;

public interface ResponsePublisherPort {

  void send(AlertDecisionMessageDto decision);

}
