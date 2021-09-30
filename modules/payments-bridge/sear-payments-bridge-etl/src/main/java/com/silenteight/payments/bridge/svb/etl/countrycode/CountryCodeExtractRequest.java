package com.silenteight.payments.bridge.svb.etl.countrycode;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.payments.bridge.svb.etl.response.SourceSystem;

import javax.annotation.Nullable;

@Value
public class CountryCodeExtractRequest {

  @NonNull SourceSystem sourceSystem;
  @NonNull String unit;
  @NonNull String ioIndicator;
  @Nullable String senderCode;
  @Nullable String receiverCode;
}
