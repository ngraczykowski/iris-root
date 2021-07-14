package com.silenteight.searpayments.scb.etl.countrycode.mts;

import lombok.NonNull;

public interface CountryCodeForUnitMap {

  @NonNull String map(@NonNull String unit);
}
