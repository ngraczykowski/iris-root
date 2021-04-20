package com.silenteight.serp.governance.model.get;

import lombok.NonNull;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;

import java.util.UUID;

public interface GetModelDetailsQuery {

  ModelDto get(@NonNull UUID modelId);
}
