package com.silenteight.hsbc.datasource.feature.nationaliddocument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;
import com.silenteight.hsbc.datasource.util.StreamUtils;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class NationalIdDocumentFeature implements FeatureValuesRetriever<NationalIdFeatureInputDto> {

  private final NationalIdDocumentQuery.Factory documentQueryFactory;

  @Override
  public NationalIdFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = documentQueryFactory.create(matchData);
    var inputBuilder = NationalIdFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyDocumentNumbers(StreamUtils.toDistinctList(query.apNationalIds()));
      inputBuilder.watchlistDocumentNumbers(StreamUtils.toDistinctList(query.mpNationalIds()));
      inputBuilder.alertedPartyCountries(StreamUtils.toDistinctList(query.apCountries()));
      inputBuilder.watchlistCountries(StreamUtils.toDistinctList(query.mpCountries()));
    } else {
      inputBuilder.alertedPartyDocumentNumbers(Collections.emptyList());
      inputBuilder.watchlistDocumentNumbers(Collections.emptyList());
      inputBuilder.alertedPartyCountries(Collections.emptyList());
      inputBuilder.watchlistCountries(Collections.emptyList());
    }

    var result = inputBuilder
        .feature(getFeatureName())
        .build();

    log.debug(
        "Datasource response for feature: {} with alerted party size {} and watchlist party size {}.",
        result.getFeature(),
        result.getAlertedPartyDocumentNumbers().size(),
        result.getWatchlistDocumentNumbers().size());

    return result;
  }

  @Override
  public Feature getFeature() {
    return Feature.NATIONAL_ID_DOCUMENT;
  }
}
