package com.silenteight.hsbc.bridge.rest.model.input;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Validated
@JsonInclude(Include.NON_NULL)
public class HsbcRecommendationRequest {

  @JsonProperty("alerts")
  @Valid
  @NotNull
  @Size(min = 1)
  private List<Alert> alerts;
}
