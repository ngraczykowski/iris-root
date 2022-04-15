package com.silenteight.sens.webapp.scb.user.sync.analyst.dto;

import lombok.Value;

import java.util.List;

@Value
public class SyncAnalystStatsDto {

  String added;
  String restored;
  String addedRole;
  String updatedDisplayName;
  String deleted;
  List<String> errors;
}
