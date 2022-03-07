package com.silenteight.fab.dataprep.domain.outgoing;

import com.silenteight.fab.dataprep.domain.model.UdsFedEvent;

public interface FeedingEventPublisher {

  void publish(UdsFedEvent event);
}
