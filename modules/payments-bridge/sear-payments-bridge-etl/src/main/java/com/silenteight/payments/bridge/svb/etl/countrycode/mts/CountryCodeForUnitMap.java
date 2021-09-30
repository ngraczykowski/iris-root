package com.silenteight.payments.bridge.svb.etl.countrycode.mts;

import lombok.NonNull;

public interface CountryCodeForUnitMap {

  @NonNull String map(@NonNull String unit);
}
