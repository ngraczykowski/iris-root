package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.sync.analyst.dto.Analyst;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AnalystFixtures {

  static final Analyst ANALYST_WITHOUT_DISPLAY_NAME = Analyst
      .builder()
      .userName("4783589366")
      .build();

  static final Analyst ANALYST_WITH_DISPLAY_NAME = Analyst
      .builder()
      .userName("1853900357")
      .displayName("5050185537")
      .build();
}
