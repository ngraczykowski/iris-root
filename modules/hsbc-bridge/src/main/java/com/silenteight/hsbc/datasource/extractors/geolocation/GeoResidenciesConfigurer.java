package com.silenteight.hsbc.datasource.extractors.geolocation;

import com.silenteight.hsbc.datasource.feature.geolocation.GeoResidencyFeatureQuery;

public class GeoResidenciesConfigurer {

  public GeoResidencyFeatureQuery.Factory create() {
    return GeoResidenciesFeatureQueryFacade::new;
  }
}
