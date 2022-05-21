package com.silenteight.hsbc.bridge.jenkins;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class CrumbResponse {

  @NotNull
  @JsonProperty("crumb")
  String crumb;

  @NotNull
  @JsonProperty("crumbRequestField")
  String crumbRequestField;
}
