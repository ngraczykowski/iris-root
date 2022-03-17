package com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

@Validated
@Data
public class GnsRtSilent8Response {

  @JsonProperty("alerts")
  @Valid
  private List<GnsRtResponseAlerts> alerts = new ArrayList<>();
}
