package com.silenteight.payments.bridge.firco.datasource.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

@Value
@Builder
public class FeatureInputUnstructuredModel {

  String alertName;

  String matchName;

  HitAndWatchlistPartyData hitAndWatchlistPartyData;

}
