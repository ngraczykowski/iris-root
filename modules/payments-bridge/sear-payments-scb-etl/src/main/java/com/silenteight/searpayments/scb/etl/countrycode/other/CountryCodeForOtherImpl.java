package com.silenteight.searpayments.scb.etl.countrycode.other;

import lombok.NonNull;

class CountryCodeForOtherImpl implements CountryCodeForOther {

  @Override
  public @NonNull String invoke(@NonNull String unit) {
    if (unit.contains("-") && UnitUtil.isValid(unit)) {
      return unit.substring(unit.indexOf('-') + 1);
    } else {
      throw new UnitCodeFormatException(unit);
    }
  }
}
