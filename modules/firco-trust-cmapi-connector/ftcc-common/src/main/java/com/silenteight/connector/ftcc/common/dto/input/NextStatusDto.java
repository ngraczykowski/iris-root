package com.silenteight.connector.ftcc.common.dto.input;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.silenteight.connector.ftcc.common.dto.validator.MinimalAlertDefinition;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class NextStatusDto {

  @NotNull(groups = MinimalAlertDefinition.class)
  @Valid
  StatusInfoDto status;
}
