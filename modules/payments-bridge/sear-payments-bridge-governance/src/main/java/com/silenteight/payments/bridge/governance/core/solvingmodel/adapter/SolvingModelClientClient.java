package com.silenteight.payments.bridge.governance.core.solvingmodel.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;
import com.silenteight.payments.bridge.governance.core.solvingmodel.model.ModelDto;
import com.silenteight.payments.bridge.governance.core.solvingmodel.port.CurrentModelClientPort;

import com.google.protobuf.Empty;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.silenteight.payments.bridge.governance.core.solvingmodel.model.ModelDto.fromSolvingModel;

@RequiredArgsConstructor
@Slf4j
class SolvingModelClientClient implements CurrentModelClientPort {

  private final SolvingModelServiceBlockingStub stub;

  private final Duration timeout;

  public ModelDto getCurrentModel() {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);
    var request = Empty.newBuilder().build();

    if (log.isTraceEnabled()) {
      log.trace("Requesting solving model: deadline={}, request={}", deadline, request);
    }

    SolvingModel defaultSolvingModel = stub.withDeadline(deadline).getDefaultSolvingModel(request);
    return fromSolvingModel(defaultSolvingModel);
  }
}
