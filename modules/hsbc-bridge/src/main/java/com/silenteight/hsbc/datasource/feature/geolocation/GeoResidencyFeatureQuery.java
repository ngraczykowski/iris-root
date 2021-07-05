package com.silenteight.hsbc.datasource.feature.geolocation;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

public interface GeoResidencyFeatureQuery {

  String getApIndividualsGeoResidencies();

  String getApEntitiesGeoResidencies();

  String getMpIndividualsGeoResidencies();

  String getMpEntitiesGeoResidencies();

  interface Factory {

    GeoResidencyFeatureQuery create(MatchData matchData);
  }
}
