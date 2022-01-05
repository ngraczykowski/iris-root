package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

@Value
@Builder
public class EtlHit {

  AlertedPartyData alertedPartyData;

  HitComposite hitComposite;

  public String getMatchId() {
    return hitComposite.getMatchId();
  }

  public String getWatchlistLocation() {
    return hitComposite.getWatchlistLocation();
  }

  public String getAlertedPartyLocation() {
    return alertedPartyData.getCtryTowns().stream().findFirst().orElse("");
  }
}
