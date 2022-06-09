/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.dto.AlertSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.port.GovernanceAlertPort;

import java.util.ArrayList;
import java.util.List;

class MockGovernanceAlertPort implements GovernanceAlertPort {

  private final List<AlertSolutionRequest> requests = new ArrayList<>();

  @Override
  public void send(AlertSolutionRequest alertSolutionRequest) {
    requests.add(alertSolutionRequest);
  }

  public int getRequestsCount() {
    return requests.size();
  }
}
