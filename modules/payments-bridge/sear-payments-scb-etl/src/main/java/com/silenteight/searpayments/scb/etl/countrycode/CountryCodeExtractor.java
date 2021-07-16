package com.silenteight.searpayments.scb.etl.countrycode;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.countrycode.mts.CountryCodeForUnitMap;
import com.silenteight.searpayments.scb.etl.countrycode.other.CountryCodeForOther;
import com.silenteight.searpayments.scb.etl.countrycode.scstar.ExtractCountryFromScstarBic;

@RequiredArgsConstructor
public class CountryCodeExtractor {

  private final CountryCodeForUnitMap extractCountryCodeForMts;
  private final ExtractCountryFromScstarBic extractCountryForScstar;
  private final CountryCodeForOther countryCodeForOther;

  @NonNull
  public String invoke(@NonNull CountryCodeExtractRequest request) {
    switch (request.getSourceSystem()) {
      case MTS:
        return extractForMts(request);
      case SCSTAR:
        return extractForScstar(request);
      default:
        return extractForOther(request);
    }
  }

  private String extractForMts(CountryCodeExtractRequest request) {
    return extractCountryCodeForMts.map(request.getUnit());
  }

  private String extractForScstar(CountryCodeExtractRequest request) {
    switch (request.getIoIndicator()) {
      case "I":
        return extractCountryForScstar.invoke(request.getReceiverCode());
      case "O":
        return extractCountryForScstar.invoke(request.getSenderCode());
      default:
        throw new IllegalArgumentException("IOIndicator must br 'I' or 'O'");
    }
  }

  private String extractForOther(CountryCodeExtractRequest request) {
    return countryCodeForOther.invoke(request.getUnit());
  }
}
