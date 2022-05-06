package com.silenteight.connector.ftcc.common.dto.input;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.silenteight.connector.ftcc.common.dto.validator.MinimalAlertDefinition;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class StatusInfoDto {

  /**
   * Identifies the database ID of the status.
   */
  @JsonProperty("ID")
  @NotNull(groups = MinimalAlertDefinition.class)
  String id;

  /**
   * Identifies the name of the status.
   */
  @NotNull(groups = MinimalAlertDefinition.class)
  String name;

  /**
   * Identifies the behavior associated to the decision made on the message.
   */
  @NotNull(groups = MinimalAlertDefinition.class)
  String routingCode;

  /**
   * Fingerprint of a status info at the time this information is built.
   */
  @NotNull(groups = MinimalAlertDefinition.class)
  String checksum;
}
