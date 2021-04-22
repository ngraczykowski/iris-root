package com.silenteight.serp.governance.model.provide.grpc;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;

public interface SolvingModelDetailsQuery {

  ModelDto get(String model);
}
