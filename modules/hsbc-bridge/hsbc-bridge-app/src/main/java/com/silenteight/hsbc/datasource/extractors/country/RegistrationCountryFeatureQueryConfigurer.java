package com.silenteight.hsbc.datasource.extractors.country;

import com.silenteight.hsbc.datasource.feature.country.RegistrationCountryFeatureQuery;

public class RegistrationCountryFeatureQueryConfigurer {

  public RegistrationCountryFeatureQuery.Factory create() {
    return RegistrationCountryFeatureQueryFacade::new;
  }
}
