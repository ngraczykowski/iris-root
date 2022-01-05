package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import java.util.List;

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

  public String getMatchedTagContent() {
    return hitComposite.getFkcoVMatchedTagContent();
  }

  public List<String> getMatchingTexts() {
    return hitComposite.getMatchingTexts();
  }

  public List<String> getSearchCodes() {
    return hitComposite.getSearchCodes();
  }

  public EntityType getEntityType() {
    return hitComposite.getEntityType();
  }

  public List<String> getWatchlistNames() {
    return hitComposite.getWatchlistNames();
  }

  public List<String> getAlertedPartyNames() {
    return alertedPartyData.getNames();
  }

  public WatchlistType getWatchlistType() {
    return hitComposite.getWatchlistType();
  }
}
