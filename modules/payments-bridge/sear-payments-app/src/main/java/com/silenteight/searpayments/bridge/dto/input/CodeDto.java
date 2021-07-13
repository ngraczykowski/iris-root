package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeDto implements Serializable {

  @JsonProperty("Name")
  String name; // "KDBKKPPY"
  @JsonProperty("Type")
  String type; // "Bic"
}
