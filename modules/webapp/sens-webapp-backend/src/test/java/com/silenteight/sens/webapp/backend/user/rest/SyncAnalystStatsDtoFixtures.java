package com.silenteight.sens.webapp.backend.user.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.sync.analyst.dto.SyncAnalystStatsDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class SyncAnalystStatsDtoFixtures {

  static final SyncAnalystStatsDto ALL_CHANGED =
      new SyncAnalystStatsDto("5 / 5", "3 / 3", "1 / 1", "7 / 7", "2 / 2");
}
