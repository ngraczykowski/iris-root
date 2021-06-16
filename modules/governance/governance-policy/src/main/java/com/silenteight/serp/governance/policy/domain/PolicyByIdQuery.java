package com.silenteight.serp.governance.policy.domain;

import java.util.UUID;

public interface PolicyByIdQuery {

  UUID getPolicyIdById(Long id);
}
