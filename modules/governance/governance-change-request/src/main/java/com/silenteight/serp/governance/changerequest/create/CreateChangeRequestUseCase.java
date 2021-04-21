package com.silenteight.serp.governance.changerequest.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;

import java.util.UUID;

@RequiredArgsConstructor
class CreateChangeRequestUseCase {

  @NonNull
  private final ChangeRequestService changeRequestService;

  UUID activate(@NonNull CreateChangeRequestCommand command) {
    return changeRequestService.addChangeRequest(
        command.getId(),
        command.getModelName(),
        command.getCreatedBy(),
        command.getCreatorComment());
  }
}
