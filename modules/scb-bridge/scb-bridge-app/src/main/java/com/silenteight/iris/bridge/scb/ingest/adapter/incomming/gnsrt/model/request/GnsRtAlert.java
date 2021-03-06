/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request;

import lombok.Data;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.GnsRtAlertStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Validated
@HasValidAlertId
public class GnsRtAlert {

  @JsonProperty("watchlistType")
  @NotNull
  private String watchlistType;

  @JsonProperty("alertStatus")
  @NotNull
  private GnsRtAlertStatus alertStatus;

  @JsonProperty("alertID")
  @NotNull
  private String alertId;

  @JsonProperty("hitList")
  @NotNull
  @Valid
  private List<GnsRtHit> hitList = new ArrayList<>();

  @JsonIgnore
  public boolean isPotentialMatch() {
    return alertStatus == GnsRtAlertStatus.POTENTIAL_MATCH;
  }
}
