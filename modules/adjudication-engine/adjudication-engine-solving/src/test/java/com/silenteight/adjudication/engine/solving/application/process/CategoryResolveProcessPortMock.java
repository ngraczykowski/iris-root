/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.port.CategoryResolvePublisherPort;

import java.util.ArrayList;
import java.util.List;

class CategoryResolveProcessPortMock implements CategoryResolvePublisherPort {

  private List<Long> alerts = new ArrayList<>();

  @Override
  public void resolve(Long alertId) {
    alerts.add(alertId);
  }

  public int getAlertsCount() {
    return alerts.size();
  }
}
