package com.silenteight.hsbc.bridge.bulk.rest.input;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Validated
@JsonInclude(Include.NON_NULL)
public class HsbcRecommendationRequest {

  @JsonProperty("bulkId")
  @Valid
  @NotNull
  private String bulkId;

  @JsonProperty("alerts")
  @Valid
  @NotNull
  @Size(min = 1)
  private List<Alert> alerts;
}
