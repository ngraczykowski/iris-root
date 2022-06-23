package com.silenteight.serp.governance.changerequest.cancel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

@RequiredArgsConstructor
public class CancelChangeRequestUseCase {

  @NonNull
  private final ChangeRequestService changeRequestService;

  public void activate(@NonNull CancelChangeRequestCommand command) {
    changeRequestService.cancel(command.getId(), command.getCancellerUsername());
  }
}
