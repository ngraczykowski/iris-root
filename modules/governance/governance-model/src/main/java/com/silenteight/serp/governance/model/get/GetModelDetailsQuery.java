package com.silenteight.serp.governance.model.get;

import lombok.NonNull;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;

import java.util.List;

public interface GetModelDetailsQuery {

  List<ModelDto> getByPolicy(@NonNull String policy);
}
