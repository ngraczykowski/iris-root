package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.firco.dto.common.StatusInfoDto;
import com.silenteight.payments.bridge.firco.dto.validator.MinimalAlertDefinition;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NextStatusDto implements Serializable {

  private static final long serialVersionUID = 5905840863163081370L;

  @JsonProperty("Status")
  @NotNull(groups = MinimalAlertDefinition.class)
  @Valid
  private StatusInfoDto status;
}
