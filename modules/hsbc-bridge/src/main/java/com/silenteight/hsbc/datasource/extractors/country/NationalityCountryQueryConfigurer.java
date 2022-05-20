package com.silenteight.hsbc.datasource.extractors.country;

import com.silenteight.hsbc.datasource.feature.country.NationalityCountryQuery;

public class NationalityCountryQueryConfigurer {

  public NationalityCountryQuery.Factory create() {
    return NationalityCountryQueryFacade::new;
  }
}
