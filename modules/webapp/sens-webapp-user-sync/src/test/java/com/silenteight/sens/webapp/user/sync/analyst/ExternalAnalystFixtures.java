package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.sync.analyst.dto.ExternalAnalyst;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ExternalAnalystFixtures {

  static final ExternalAnalyst ANALYST_WITHOUT_DISPLAY_NAME = ExternalAnalyst
      .builder()
      .userName("4783589366")
      .build();

  static final ExternalAnalyst ANALYST_WITH_DISPLAY_NAME = ExternalAnalyst
      .builder()
      .userName("1853900357")
      .displayName("5050185537")
      .build();
}
