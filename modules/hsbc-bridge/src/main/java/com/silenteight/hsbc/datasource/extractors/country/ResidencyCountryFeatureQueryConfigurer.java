package com.silenteight.hsbc.datasource.extractors.country;

import com.silenteight.hsbc.datasource.feature.country.ResidencyCountryFeatureQuery;

public class ResidencyCountryFeatureQueryConfigurer {

  public ResidencyCountryFeatureQuery.Factory getFactory() {
    return ResidencyCountryFeatureQueryFacade::new;
  }
}
