package com.silenteight.scb.feeding.domain.port.outgoing;

import com.silenteight.scb.feeding.domain.model.UdsFedEvent;

public interface FeedingEventPublisher {

  void publish(UdsFedEvent event);
}
