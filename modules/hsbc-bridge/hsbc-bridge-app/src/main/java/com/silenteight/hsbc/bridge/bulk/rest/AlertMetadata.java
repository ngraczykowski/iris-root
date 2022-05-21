package com.silenteight.hsbc.bridge.bulk.rest;

import lombok.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonInclude(Include.NON_NULL)
@Value
public class AlertMetadata {

  @JsonProperty("key")
  @NotNull
  @NotBlank
  String key;

  @JsonProperty("value")
  @NotNull
  String value;

}
