package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class PlaceOfBirthDto implements Serializable {

  private static final long serialVersionUID = 2473217525872487820L;
  @JsonProperty("PlaceOfBirth")
  String placeOfBirth;
}
