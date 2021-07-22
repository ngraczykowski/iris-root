package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameDto implements Serializable {

  private static final long serialVersionUID = 8259704647182442134L;
  @JsonProperty("Name")
  String name; // "OFAC"
}
