package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RulesContextDto implements Serializable {

  private static final long serialVersionUID = -8919235980086784741L;
  @JsonProperty("Type")
  String type; // "1"
  @JsonProperty("Priority")
  String priority; // "0"
  @JsonProperty("Confidentiality")
  String confidentiality; // "0"
  @JsonProperty("Info")
  String info; // "none"
}
