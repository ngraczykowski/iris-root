package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.Data;

import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConstructorBinding
class SyncAnalystProperties {

  @OracleRelationName
  private String userDbRelationName;

  private int maxErrors;
}
