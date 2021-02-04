package com.silenteight.serp.governance.policy.solve;

import java.util.Optional;
import java.util.UUID;

public interface InUsePolicyQuery {

  Optional<UUID> getPolicyInUse();
}
