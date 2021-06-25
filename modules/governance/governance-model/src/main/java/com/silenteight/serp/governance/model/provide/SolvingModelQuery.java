package com.silenteight.serp.governance.model.provide;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;

public interface SolvingModelQuery {

  SolvingModel get(ModelDto modelDto);
}
