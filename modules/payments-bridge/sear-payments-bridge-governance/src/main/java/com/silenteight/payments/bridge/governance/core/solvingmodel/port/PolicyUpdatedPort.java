package com.silenteight.payments.bridge.governance.core.solvingmodel.port;

import com.silenteight.proto.payments.bridge.internal.v1.event.PolicyUpdated;

public interface PolicyUpdatedPort {
  void send(PolicyUpdated policyUpdated);
}
