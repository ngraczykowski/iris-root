package com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Data
@Builder
public class GnsRtResponseMatch {

  @JsonProperty("hitID")
  @NotNull
  private String hitID;

  @JsonProperty("stepId")
  @NotNull
  private String stepId;

  @JsonProperty("fvSignature")
  @NotNull
  private String fvSignature;

}
