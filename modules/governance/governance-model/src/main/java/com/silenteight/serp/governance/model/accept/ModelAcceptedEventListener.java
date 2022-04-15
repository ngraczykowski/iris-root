package com.silenteight.serp.governance.model.accept;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.approve.event.ModelAcceptedEvent;
import com.silenteight.serp.governance.model.common.ModelResource;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.model.used.ModelDeployedEvent;
import com.silenteight.serp.governance.policy.promote.PromotePolicyCommand;
import com.silenteight.serp.governance.policy.promote.PromotePolicyUseCase;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
class ModelAcceptedEventListener {

  @NonNull
  private final ModelDetailsQuery modelDetailsQuery;
  @NonNull
  private final PromotePolicyUseCase promotePolicyUseCase;
  @NonNull
  private final SendPromoteMessageUseCase sendPromoteMessageUseCase;
  @NonNull
  private final ModelTransfer modelTransfer;
  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  public void handle(@NonNull ModelAcceptedEvent event) {
    // INFO(kdzieciol): Fetching a whole modelDto as we will need strategy and config set very soon
    ModelDto modelDto = modelDetailsQuery.get(ModelResource.fromResourceName(event.getModelName()));
    promotePolicyUseCase.activate(PromotePolicyCommand.of(
        event.getCorrelationId(), modelDto.getPolicy(), event.getPromotedBy()));
    // INFO(kdzieciol): Here we will call promoteUseCase for strategy and agent config set

    sendPromoteMessageUseCase.activate(SendPromoteMessageCommand.of(
        event.getCorrelationId(), event.getModelName(), modelDto.getModelVersion()));

    if (modelTransfer == ModelTransfer.LOCAL)
      eventPublisher.publishEvent(ModelDeployedEvent.of(event.getModelName()));
  }
}
