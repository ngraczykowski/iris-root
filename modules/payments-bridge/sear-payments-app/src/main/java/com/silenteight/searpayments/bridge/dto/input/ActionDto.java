package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ActionDto implements Serializable {

  private static final long serialVersionUID = -1149520199768792368L;
  @JsonProperty("DateTime")
  String dateTime; // "20200909074059"
  @JsonProperty("Operator")
  String operator; // "Filter Engine"
  @JsonProperty("Comment")
  String comment; // "Stop: 2, Nonblocking: 0"
  @JsonProperty("Status")
  StatusDto status;
}
