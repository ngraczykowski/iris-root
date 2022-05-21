package com.silenteight.hsbc.datasource.extractors.country;

import com.silenteight.hsbc.datasource.feature.country.OtherCountryQuery;

public class OtherCountryQueryConfigurer {

  public OtherCountryQuery.Factory create() {
    return OtherCountryQueryFacade::new;
  }
}
