package com.silenteight.hsbc.datasource.extractors.country;

import com.silenteight.hsbc.datasource.feature.location.LocationFeatureQuery;

public class LocationFeatureQueryConfigurer {

  public LocationFeatureQuery.Factory getFactory() {
    return LocationFeatureQueryFacade::new;
  }
}
