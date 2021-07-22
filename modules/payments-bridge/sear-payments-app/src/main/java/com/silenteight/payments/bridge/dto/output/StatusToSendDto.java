package com.silenteight.payments.bridge.dto.output;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
public class StatusToSendDto implements Serializable {

  private static final long serialVersionUID = 5143132093901080553L;
  @JsonProperty("ID")
  String id;
  @JsonProperty("Name")
  String name;
  @JsonProperty("RoutingCode")
  String routingCode;
  @JsonProperty("Checksum")
  String checksum;
}
