package com.silenteight.payments.bridge.firco.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.firco.dto.validator.MinimalAlertDefinition;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusInfoDto implements Serializable {

  private static final long serialVersionUID = 1202038844291055591L;

  /**
   * Identifies the database ID of the status.
   */
  @JsonProperty("ID")
  @NotNull(groups = MinimalAlertDefinition.class)
  private String id;

  /**
   * Identifies the name of the status.
   */
  @JsonProperty("Name")
  @NotNull(groups = MinimalAlertDefinition.class)
  private String name;

  /**
   * Identifies the behavior associated to the decision made on the message.
   */
  @JsonProperty("RoutingCode")
  @NotNull(groups = MinimalAlertDefinition.class)
  @JsonFormat
  private String routingCode;

  /**
   * Fingerprint of a status info at the time this information is built.
   */
  @JsonProperty("Checksum")
  @NotNull(groups = MinimalAlertDefinition.class)
  private String checksum;

}
