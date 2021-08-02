package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficialReferenceDto implements Serializable {

  private static final long serialVersionUID = 561005476633592762L;

  @JsonProperty("Name")
  private String name; // "CORPORATE"
}
