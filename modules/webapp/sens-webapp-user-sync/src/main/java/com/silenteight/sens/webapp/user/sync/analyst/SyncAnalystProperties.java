package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.Data;

import org.springframework.validation.annotation.Validated;

@Data
@Validated
class SyncAnalystProperties {

  @OracleRelationName
  private String userDbRelationName;
}
