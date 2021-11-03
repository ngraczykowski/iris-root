package com.silenteight.hsbc.datasource.feature.geolocation;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.name.Party;
import com.silenteight.hsbc.datasource.feature.country.CountryDiscoverer;

import java.util.List;

public interface GeoResidencyFeatureQuery {

  String getApIndividualsGeoResidencies(Party party);

  String getApEntitiesGeoResidencies(List<String> entitiesAlertedPartyNames);

  String getMpIndividualsGeoResidencies();

  String getMpEntitiesGeoResidencies();

  interface Factory {

    GeoResidencyFeatureQuery create(MatchData matchData, CountryDiscoverer countryDiscoverer);
  }
}
