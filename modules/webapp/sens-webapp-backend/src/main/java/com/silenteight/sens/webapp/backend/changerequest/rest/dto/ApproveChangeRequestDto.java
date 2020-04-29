package com.silenteight.sens.webapp.backend.changerequest.rest.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestCommand;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ApproveChangeRequestDto {

  @NonNull
  private Long changeRequestId;

  public ApproveChangeRequestCommand toCommand(@NonNull String username) {
    return ApproveChangeRequestCommand.builder()
        .changeRequestId(changeRequestId)
        .approverUsername(username)
        .build();
  }
}
