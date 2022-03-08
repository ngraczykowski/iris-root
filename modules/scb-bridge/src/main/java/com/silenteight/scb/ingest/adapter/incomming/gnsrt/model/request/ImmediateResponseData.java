package com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request;

import lombok.Data;

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.GnsRtAlertStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
@Validated
public class ImmediateResponseData {

  @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]", timezone = "Asia/Hong_Kong")
  @JsonProperty("immediateResponseTimestamp")
  @NotNull
  private Instant immediateResponseTimestamp;

  @JsonProperty("overAllStatus")
  private GnsRtAlertStatus overAllStatus;

  @JsonProperty("alerts")
  @Valid
  @NotNull
  @AtLeastOnePotentialMatch
  @AllPotentialMatchesHasValidAlertId
  private List<GnsRtAlert> alerts = new ArrayList<>();
}
