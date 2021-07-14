package com.silenteight.hsbc.bridge.model.dto;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelInfo {

  @NotNull
  @JsonProperty("name")
  String name;

  @NotNull
  @JsonProperty("url")
  String url;

  @NotNull
  @JsonProperty("type")
  String type;

  @NotNull
  @JsonProperty("changeType")
  String changeType;
}
