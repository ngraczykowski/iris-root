/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.infrastructure.util.MockUtils;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsOut;

@Slf4j
class AgentInputServiceClientMock implements AgentInputServiceClient {

  @Override
  public <T extends Feature> BatchCreateAgentInputsOut createBatchCreateAgentInputs(
      BatchCreateAgentInputsIn<T> request) {
    log.info("MOCK: createBatchCreateAgentInputs");
    MockUtils.randomSleep(10, 20);
    return BatchCreateAgentInputsOut.builder().build();
  }

}
