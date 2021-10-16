package com.silenteight.payments.bridge.svb.oldetl.response;

import lombok.Value;

@Value
public class HitData {

  AlertedPartyData alertedPartyData;
  HitAndWatchlistPartyData hitAndWlPartyData;
}
