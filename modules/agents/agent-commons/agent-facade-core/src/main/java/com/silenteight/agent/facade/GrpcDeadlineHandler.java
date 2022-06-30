package com.silenteight.agent.facade;

import com.google.protobuf.Timestamp;

import static java.time.Instant.now;

public class GrpcDeadlineHandler {

  public static void checkRequestTimeout(Timestamp deadlineTime) {
    if (deadlineTimeInitialized(deadlineTime)
        && now().getEpochSecond() > deadlineTime.getSeconds()) {
      throw new AgentRequestTimeoutException(deadlineTime.getSeconds());
    }
  }

  public static boolean deadlineTimeInitialized(Timestamp timestamp) {
    return timestamp.getSeconds() > 0;
  }
}
