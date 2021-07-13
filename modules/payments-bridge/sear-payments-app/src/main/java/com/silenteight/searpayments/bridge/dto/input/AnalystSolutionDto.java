package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.tsaas.bridge.dto.validator.MinimalAlertDefinition;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalystSolutionDto implements Serializable {

  private static final long serialVersionUID = -7868232254808751898L;
  @JsonProperty("SystemID")
  @NotNull(groups = MinimalAlertDefinition.class)
  String systemId; // "USMTS20200909074058-91828-188490"
  @JsonProperty("Actions")
  List<RequestActionDto> actions;
}
