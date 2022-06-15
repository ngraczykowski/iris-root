package com.silenteight.connector.ftcc.common.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class CaseManagerAuthenticationDto implements Serializable {

  private static final long serialVersionUID = 1172622409389133221L;

  /**
   * Case manager login.
   */
  //  @NotEmpty(groups = MinimalAlertDefinition.class)
  private String userLogin;

  /**
   * Case manager password.
   */
  @ToString.Exclude
  //  @NotEmpty(groups = MinimalAlertDefinition.class)
  private String userPassword;

  /**
   * Case manager realm.
   */
  private String userRealm;
}
