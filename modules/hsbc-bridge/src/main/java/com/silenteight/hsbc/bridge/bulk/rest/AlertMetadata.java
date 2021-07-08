package com.silenteight.hsbc.bridge.bulk.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonInclude(Include.NON_NULL)
public class AlertMetadata {

  @JsonProperty("key")
  @NotNull
  @NotBlank
  private String key;

  @JsonProperty("value")
  @NotNull
  @NotBlank
  private String value;


  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
