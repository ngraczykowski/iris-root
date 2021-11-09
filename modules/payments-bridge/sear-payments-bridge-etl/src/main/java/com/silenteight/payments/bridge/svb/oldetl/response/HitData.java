package com.silenteight.payments.bridge.svb.oldetl.response;

import lombok.Value;

import java.util.Optional;

@Value
public class HitData {

  String matchId;
  AlertedPartyData alertedPartyData;
  HitAndWatchlistPartyData hitAndWlPartyData;

  public Optional<String> getAlertedPartyAccountNumberOrFirstName() {
    return alertedPartyData.getAccountNumberOrFirstName();
  }
}
