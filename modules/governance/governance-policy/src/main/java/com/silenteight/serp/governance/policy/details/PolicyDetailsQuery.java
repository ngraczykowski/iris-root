package com.silenteight.serp.governance.policy.details;

import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import java.util.UUID;

public interface PolicyDetailsQuery {

  PolicyDto details(@NonNull UUID id);
}
