package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ApproveChangeRequestUseCaseFixtures {

  static final ApproveChangeRequestCommand APPROVE_COMMAND =
      ApproveChangeRequestCommand.builder()
      .changeRequestId(5L)
      .approverUsername("approver")
      .build();
}
