package com.silenteight.payments.bridge.svb.etl.response;

import lombok.Value;

@Value
public class HitData {

  AlertedPartyData alertedPartyData;
  HitAndWatchlistPartyData hitAndWlPartyData;
}
