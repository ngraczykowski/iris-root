package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.firco.adapter.incoming.dto.output.AlertDecisionMessageDto;

public interface ResponsePublisherPort {

  void send(AlertDecisionMessageDto decision);

}
