package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RejectChangeRequestUseCaseFixtures {

  static final RejectChangeRequestCommand REJECT_COMMAND =
      RejectChangeRequestCommand.builder()
      .changeRequestId(5L)
      .rejectorUsername("rejector")
      .build();
}
