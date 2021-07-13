package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.tsaas.bridge.dto.validator.MinimalAlertDefinition;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto implements Serializable {

  private static final long serialVersionUID = 1202038844291055591L;
  @JsonProperty("ID")
  @NotNull(groups = MinimalAlertDefinition.class)
  String id; // "224"
  @JsonProperty("Name")
  @NotNull(groups = MinimalAlertDefinition.class)
  String name; // "CASE"
  @JsonProperty("RoutingCode")
  @NotNull(groups = MinimalAlertDefinition.class)
  String routingCode; // "12"
  @JsonProperty("Checksum")
  @NotNull(groups = MinimalAlertDefinition.class)
  String checksum; // "0d3df7da579d81e08fefa9c5774009c7"

  // For openapi document only
  private enum AnalystSolution {
    NO_SOLUTION,
    FALSE_POSITIVE,
    POTENTIAL_TRUE_POSITIVE,
    TRUE_POSITIVE,
    OTHER,
    UNRECOGNIZED
  }
}
