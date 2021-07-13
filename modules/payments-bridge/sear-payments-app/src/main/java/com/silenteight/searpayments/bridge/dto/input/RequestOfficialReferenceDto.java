package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOfficialReferenceDto implements Serializable {

  private static final long serialVersionUID = 8177453022740418039L;
  @JsonProperty("OfficialReference")
  OfficialReferenceDto officialReference;
}
