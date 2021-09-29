package com.silenteight.payments.bridge.firco.adapter.incoming.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.firco.adapter.incoming.dto.common.StatusInfoDto;
import com.silenteight.payments.bridge.firco.adapter.incoming.dto.validator.MinimalAlertDefinition;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class NextStatusDto implements Serializable {

  private static final long serialVersionUID = 5905840863163081370L;

  @NotNull(groups = MinimalAlertDefinition.class)
  @Valid
  private StatusInfoDto status;
}
