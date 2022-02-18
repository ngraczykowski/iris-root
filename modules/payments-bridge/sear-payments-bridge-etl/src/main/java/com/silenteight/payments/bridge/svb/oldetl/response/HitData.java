package com.silenteight.payments.bridge.svb.oldetl.response;

import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Value
public class HitData {

  String matchId;
  AlertedPartyData alertedPartyData;
  HitAndWatchlistPartyData hitAndWlPartyData;

  public Optional<String> getAlertedPartyAccountNumberOrFirstName() {
    return alertedPartyData.getAccountNumberOrFirstName();
  }

  public String getTag() {
    return hitAndWlPartyData.getTag();
  }

  public String getFieldValue() {
    return Optional.ofNullable(hitAndWlPartyData.getTag())
        .orElse("");
  }

  public String getMatchingText() {
    return Optional.ofNullable(hitAndWlPartyData.getMatchingText())
        .orElse("");
  }

  public SolutionType getSolutionType() {
    return hitAndWlPartyData.getSolutionType();
  }

  public List<String> getSearchCodes() {
    return Optional
        .ofNullable(hitAndWlPartyData.getSearchCodes())
        .orElseGet(Collections::emptyList);
  }

  public List<String> getPassports() {
    return Optional.ofNullable(hitAndWlPartyData.getPassports())
        .orElseGet(Collections::emptyList);
  }

  public List<String> getNatIds() {
    return Optional.ofNullable(hitAndWlPartyData.getNatIds())
        .orElseGet(Collections::emptyList);
  }

  public List<String> getBics() {
    return Optional.ofNullable(hitAndWlPartyData.getBics())
        .orElseGet(Collections::emptyList);
  }
}
