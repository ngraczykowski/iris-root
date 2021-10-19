package com.silenteight.serp.governance.model.used;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.provide.SolvingModelQuery;
import com.silenteight.serp.governance.model.used.amqp.ModelUsedOnProductionMessageGateway;

@RequiredArgsConstructor
class SendModelUsedOnProductionUseCase {

  @NonNull
  private final ModelUsedOnProductionMessageGateway messageGateway;
  @NonNull
  private final SolvingModelQuery solvingModelQuery;

  public void activate(ModelDto modelDto) {
    SolvingModel solvingModel = solvingModelQuery.get(modelDto);
    messageGateway.send(solvingModel);
  }
}
