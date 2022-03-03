package com.silenteight.customerbridge.gnsrt.model.request;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class GnsRtHit {

  @JsonProperty("hitID")
  private String hitId;

  @JsonProperty("hitDetails")
  private String hitDetails;
}
