package com.silenteight.searpayments.scb.etl.countrycode.other;

import lombok.NonNull;

public interface CountryCodeForOther {

  @NonNull String invoke(@NonNull String unit);

}
