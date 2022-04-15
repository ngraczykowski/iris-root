package com.silenteight.serp.governance.model.get;

import lombok.NonNull;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;

import java.util.List;
import java.util.UUID;

public interface ModelDetailsQuery {

  ModelDto get(@NonNull UUID id);

  List<ModelDto> getByPolicy(@NonNull String policy);

  UUID getModelIdByVersion(String version);
}
