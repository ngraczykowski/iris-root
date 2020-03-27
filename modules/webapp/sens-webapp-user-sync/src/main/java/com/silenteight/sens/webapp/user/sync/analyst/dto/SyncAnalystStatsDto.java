package com.silenteight.sens.webapp.user.sync.analyst.dto;

import lombok.Value;

@Value
public class SyncAnalystStatsDto {

  String added;
  String restored;
  String addedRole;
  String updatedDisplayName;
  String deleted;
}
