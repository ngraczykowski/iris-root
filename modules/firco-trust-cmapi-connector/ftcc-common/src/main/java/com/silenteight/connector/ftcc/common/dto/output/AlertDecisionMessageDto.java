package com.silenteight.connector.ftcc.common.dto.output;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import javax.validation.Valid;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
@JsonIgnoreProperties({ "UserLogin", "UserPassword" })
public class AlertDecisionMessageDto {

  String unit;

  String businessUnit;

  String messageID;

  String systemID;

  @Valid
  StatusInfoDto status;

  String comment;

  String operator;

  @JsonInclude(Include.NON_NULL)
  AttachmentDto attachment;

  String userLogin;

  @ToString.Exclude
  String userPassword;
}
