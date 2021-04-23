package com.silenteight.serp.governance.changerequest.approve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

@RequiredArgsConstructor
class ApproveChangeRequestUseCase {

  @NonNull
  private final ChangeRequestService changeRequestService;

  void activate(@NonNull ApproveChangeRequestCommand command) {
    changeRequestService.approve(
        command.getId(),
        command.getApproverUsername(),
        command.getApproverComment());
  }
}
