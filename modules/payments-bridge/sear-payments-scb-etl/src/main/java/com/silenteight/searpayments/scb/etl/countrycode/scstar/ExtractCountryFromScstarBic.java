package com.silenteight.searpayments.scb.etl.countrycode.scstar;

import lombok.NonNull;

public interface ExtractCountryFromScstarBic {

  @NonNull String invoke(@NonNull String bic);

  static ExtractCountryFromScstarBic defaultExtract() {
    return new ExtractCountryFromScstarBicImpl();
  }
}
