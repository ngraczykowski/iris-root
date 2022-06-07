/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.port.ReadyMatchFeatureVectorPort;

import java.util.ArrayList;
import java.util.List;

class ReadyMatchFeatureVectorPortMock implements ReadyMatchFeatureVectorPort {

  private List<MatchSolutionRequest> requestList = new ArrayList<>();

  @Override
  public void send(MatchSolutionRequest matchSolutionRequest) {
    requestList.add(matchSolutionRequest);
  }

  public int getRequestsCount() {
    return requestList.size();
  }
}
