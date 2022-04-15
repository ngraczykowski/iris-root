package com.silenteight.sens.webapp.scb.user.sync.analyst;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.SyncAnalystStatsDto;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class SyncAnalystStatsDtoFixtures {

  static final SyncAnalystStatsDto ALL_CHANGED_WITH_ONE_ERROR =
      new SyncAnalystStatsDto("5 / 6", "3 / 3", "1 / 1", "7 / 7", "2 / 2",
          List.of("some error occurred adding user abc"));
}
