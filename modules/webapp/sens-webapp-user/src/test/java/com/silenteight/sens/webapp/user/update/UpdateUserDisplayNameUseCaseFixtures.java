package com.silenteight.sens.webapp.user.update;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.common.testing.time.MockTimeSource;
import com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCase.UpdateUserDisplayNameCommand;

import java.time.OffsetDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UpdateUserDisplayNameUseCaseFixtures {

  private static final MockTimeSource MOCK_TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;

  static final OffsetDateTime OFFSET_DATE_TIME = MOCK_TIME_SOURCE.offsetDateTime();

  static final UpdateUserDisplayNameCommand NEW_DISPLAY_NAME_COMMAND =
      UpdateUserDisplayNameCommand
          .builder()
          .username("jsmith")
          .displayName("John Smith")
          .timeSource(MOCK_TIME_SOURCE)
          .build();
}
