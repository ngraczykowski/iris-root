package com.silenteight.hsbc.datasource.feature.geolocation;

import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;

public interface GeoPlaceOfBirthFeatureQuery {

  String getApGeoPlacesOfBirth();

  String getMpGeoPlaceOfBirth();

  interface Factory {

    GeoPlaceOfBirthFeatureQuery create(IndividualComposite individualComposite);
  }
}
