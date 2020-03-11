package com.silenteight.sens.webapp.grpc;

import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

interface GrpcTimeoutConfig {

  Duration getTimeout();

  default Deadline getDeadline() {
    return Deadline.after(getTimeout().getSeconds(), TimeUnit.SECONDS);
  }
}
