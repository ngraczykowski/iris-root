package com.silenteight.serp.governance.model.provide.grpc;

import lombok.NonNull;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;

public interface SolvingModelDetailsQuery {

  ModelDto get(@NonNull  String model);
}
