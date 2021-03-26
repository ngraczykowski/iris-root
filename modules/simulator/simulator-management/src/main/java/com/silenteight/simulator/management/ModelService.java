package com.silenteight.simulator.management;

import lombok.NonNull;

import com.silenteight.model.api.v1.SolvingModel;

public interface ModelService {

  SolvingModel getModel(@NonNull String modelName);
}
