package com.silenteight.searpayments.scb.etl.countrycode.mts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
class CountryCodeForUnitMapImpl implements CountryCodeForUnitMap {

  @NonNull private final List<UnitCountryCodeTuple> map;

  @NonNull public String map(@NonNull String unit) {
    for (UnitCountryCodeTuple tuple : map) {
      if (tuple.unit.equals(unit))
        return tuple.countryCode;
    }
    throw new NoSuchElementException("Could not find country for unit " + unit);
  }

  @Value
  static class UnitCountryCodeTuple {
    String unit;
    String countryCode;
  }
}
