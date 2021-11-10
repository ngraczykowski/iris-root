package com.silenteight.hsbc.datasource.extractors.ispep;

import com.silenteight.hsbc.datasource.extractors.common.SignType;

import java.util.stream.Stream;

class CountryCodesSplitter {

  static Stream<String> splitCountryCodesBySpace(String countryCodes) {
    return Stream.of(countryCodes.split(SignType.SPACE.getSign()));
  }
}
