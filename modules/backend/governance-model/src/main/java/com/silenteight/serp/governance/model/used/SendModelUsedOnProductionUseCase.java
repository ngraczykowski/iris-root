package com.silenteight.serp.governance.model.used;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.provide.SolvingModelQuery;
import com.silenteight.serp.governance.model.used.amqp.ModelUsedOnProductionMessageGateway;

@RequiredArgsConstructor
@Slf4j
class SendModelUsedOnProductionUseCase {

  @NonNull
  private final ModelUsedOnProductionMessageGateway messageGateway;
  @NonNull
  private final SolvingModelQuery solvingModelQuery;

  public void activate(ModelDto modelDto) {
    log.info("Sending an event message with a new model ({}).", modelDto.getName());

    SolvingModel solvingModel = solvingModelQuery.get(modelDto);
    messageGateway.send(solvingModel);

    log.debug("Event message with a new model ({}). sent.", modelDto.getName());
  }
}
