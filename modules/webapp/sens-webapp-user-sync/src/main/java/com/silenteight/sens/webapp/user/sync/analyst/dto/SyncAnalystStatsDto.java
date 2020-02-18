package com.silenteight.sens.webapp.user.sync.analyst.dto;

import lombok.Value;

@Value
public class SyncAnalystStatsDto {

  int added;
  int updatedRole;
  int updatedDisplayName;
  int deleted;
}
