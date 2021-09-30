package com.silenteight.payments.bridge.svb.etl.countrycode.other;

import lombok.NonNull;

public interface CountryCodeForOther {

  @NonNull String invoke(@NonNull String unit);

}
