package com.silenteight.serp.governance.policy.featurevector;

import java.util.UUID;

public interface PolicyByIdQuery {

  UUID getPolicyIdById(Long id);
}
