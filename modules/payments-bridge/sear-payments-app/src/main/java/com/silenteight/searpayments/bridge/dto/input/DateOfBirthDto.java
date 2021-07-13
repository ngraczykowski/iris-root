package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class DateOfBirthDto implements Serializable {

  private static final long serialVersionUID = 2714143439621431143L;
  @JsonProperty("DateOfBirth")
  String dateOfBirth;
}
