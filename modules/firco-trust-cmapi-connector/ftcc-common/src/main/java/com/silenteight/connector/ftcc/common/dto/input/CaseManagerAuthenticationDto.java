package com.silenteight.connector.ftcc.common.dto.input;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class CaseManagerAuthenticationDto {

  /**
   * Case manager login.
   */
  //  @NotEmpty(groups = MinimalAlertDefinition.class)
  String userLogin;

  /**
   * Case manager password.
   */
  @ToString.Exclude
  //  @NotEmpty(groups = MinimalAlertDefinition.class)
  String userPassword;

  /**
   * Case manager realm.
   */
  String userRealm;
}
