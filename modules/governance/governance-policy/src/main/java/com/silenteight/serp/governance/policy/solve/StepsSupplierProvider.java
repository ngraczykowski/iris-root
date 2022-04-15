package com.silenteight.serp.governance.policy.solve;

import java.util.UUID;

public interface StepsSupplierProvider {

  StepsSupplier getStepsSupplier(UUID policyId);
}
