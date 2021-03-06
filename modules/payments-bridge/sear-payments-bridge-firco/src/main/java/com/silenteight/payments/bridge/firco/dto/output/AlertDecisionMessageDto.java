package com.silenteight.payments.bridge.firco.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.silenteight.payments.bridge.firco.dto.input.StatusInfoDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
@JsonIgnoreProperties({ "UserLogin", "UserPassword" })
public class AlertDecisionMessageDto implements Serializable {

  private static final long serialVersionUID = -6851877425739319284L;

  private String unit;

  private String businessUnit;

  private String messageID;

  private String systemID;

  private StatusInfoDto status;

  private String comment;

  private String operator;

  @JsonInclude(Include.NON_NULL)
  private AttachmentDto attachment;

  private List<AlertDecisionActionDto> actions;

  private String userLogin;

  @ToString.Exclude
  private String userPassword;
}
