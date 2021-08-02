package com.silenteight.payments.bridge.firco.dto.input;

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
  private String name; // "KDBKKPPY"

  @JsonProperty("Type")
  private String type; // "Bic"
}
