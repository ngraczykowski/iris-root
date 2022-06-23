package com.silenteight.serp.governance.changerequest.reject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

@RequiredArgsConstructor
class RejectChangeRequestUseCase {

  @NonNull
  private final ChangeRequestService changeRequestService;

  void activate(@NonNull RejectChangeRequestCommand command) {
    changeRequestService.reject(
        command.getId(),
        command.getRejectorUsername(),
        command.getRejectorComment());
  }
}
