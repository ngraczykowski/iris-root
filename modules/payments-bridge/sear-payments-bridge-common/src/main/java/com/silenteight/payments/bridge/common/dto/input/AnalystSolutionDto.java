package com.silenteight.payments.bridge.common.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.common.dto.validator.MinimalAlertDefinition;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class AnalystSolutionDto implements Serializable {

  private static final long serialVersionUID = -7868232254808751898L;

  @JsonProperty("SystemID")
  @NotNull(groups = MinimalAlertDefinition.class)
  private String systemId; // "USMTS20200909074058-91828-188490"

  private List<RequestActionDto> actions;
}
