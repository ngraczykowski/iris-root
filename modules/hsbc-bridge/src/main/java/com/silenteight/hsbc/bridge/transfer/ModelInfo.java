package com.silenteight.hsbc.bridge.transfer;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;
import javax.validation.constraints.NotNull;

@Validated
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelInfo {

  @NotNull
  @JsonProperty("id")
  UUID id;

  @NotNull
  @JsonProperty("modelName")
  String modelName;

  @NotNull
  @JsonProperty("url")
  String url;
}
