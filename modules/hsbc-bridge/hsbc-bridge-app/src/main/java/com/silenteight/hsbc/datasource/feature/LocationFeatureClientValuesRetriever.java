package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.location.LocationFeatureInputDto;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.hsbc.datasource.feature.country.CountryDiscoverer;

public interface LocationFeatureClientValuesRetriever extends Retriever {

  LocationFeatureInputDto retrieve(MatchData matchData, CountryDiscoverer countryDiscoverer, NameInformationServiceClient nameInformationServiceClient);
}
