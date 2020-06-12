package com.silenteight.sens.webapp.backend.changerequest.cancel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class CancelChangeRequestUseCaseFixtures {

  static final CancelChangeRequestCommand CANCEL_COMMAND =
      CancelChangeRequestCommand.builder()
          .changeRequestId(5L)
          .cancellerUsername("canceller")
          .build();
}
