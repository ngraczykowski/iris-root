package com.silenteight.serp.governance.changerequest.approve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.approve.event.ModelAcceptedEvent;
import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
class ApproveChangeRequestUseCase {

  @NonNull
  private final ChangeRequestService changeRequestService;
  @NonNull
  private final ChangeRequestModelQuery changeRequstModelQuery;
  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  void activate(@NonNull ApproveChangeRequestCommand command) {
    String modelName = changeRequstModelQuery.getModel(command.getId());
    eventPublisher.publishEvent(ModelAcceptedEvent.of(
        command.getCorrelationId(), modelName, command.getApproverUsername()));
    changeRequestService.approve(
        command.getId(),
        command.getApproverUsername(),
        command.getApproverComment());
  }
}
