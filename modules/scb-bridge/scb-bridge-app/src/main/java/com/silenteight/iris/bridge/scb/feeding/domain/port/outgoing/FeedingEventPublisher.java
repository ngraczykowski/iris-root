/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.domain.port.outgoing;

import com.silenteight.iris.bridge.scb.feeding.domain.model.UdsFedEvent;

public interface FeedingEventPublisher {

  void publish(UdsFedEvent event);
}
