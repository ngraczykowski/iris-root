package com.silenteight.scb.feeding.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsOut;

import org.apache.commons.lang3.RandomUtils;

@Slf4j
class AgentInputServiceClientMock implements AgentInputServiceClient {

  @Override
  public <T extends Feature> BatchCreateAgentInputsOut createBatchCreateAgentInputs(
      BatchCreateAgentInputsIn<T> request) {
    log.info("MOCK: createBatchCreateAgentInputs");
    randomSleep();
    return BatchCreateAgentInputsOut.builder().build();
  }

  private static void randomSleep() {
    try {
      Thread.sleep(RandomUtils.nextInt(10, 20));
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }
}
