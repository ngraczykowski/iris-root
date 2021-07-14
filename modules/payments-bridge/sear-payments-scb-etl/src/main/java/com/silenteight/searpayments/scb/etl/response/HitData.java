package com.silenteight.searpayments.scb.etl.response;

import lombok.Value;

@Value
public class HitData {
  AlertedPartyData alertedPartyData;
  HitAndWatchlistPartyData hitAndWlPartyData;
}
