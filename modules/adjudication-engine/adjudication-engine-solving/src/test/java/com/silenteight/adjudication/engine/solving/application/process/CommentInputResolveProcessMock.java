/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.port.CommentInputResolvePublisherPort;

import java.util.ArrayList;
import java.util.List;

class CommentInputResolveProcessMock implements CommentInputResolvePublisherPort {

  private List<String> alerts = new ArrayList<>();

  @Override
  public void resolve(String alert) {
    alerts.add(alert);
  }

  public int getAlertsCount() {
    return alerts.size();
  }
}
