package com.silenteight.hsbc.bridge.jenkins;

import lombok.Data;

import com.silenteight.hsbc.bridge.transfer.ModelClient.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class ModelUpdateResponse implements Model {

  @NotNull
  @JsonProperty("version")
  String version;

  @NotNull
  @JsonProperty("url")
  String url;
}
