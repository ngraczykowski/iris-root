package com.silenteight.hsbc.datasource.extractors.geolocation;

import com.silenteight.hsbc.datasource.feature.geolocation.GeoPlaceOfBirthFeatureQuery;

public class GeoPlaceOfBirthConfigurer {

  public GeoPlaceOfBirthFeatureQuery.Factory create() {
    return GeoPlaceOfBirthFeatureQueryFacade::new;
  }
}
