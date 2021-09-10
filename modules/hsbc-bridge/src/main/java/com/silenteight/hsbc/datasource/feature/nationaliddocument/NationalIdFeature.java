package com.silenteight.hsbc.datasource.feature.nationaliddocument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
public class NationalIdFeature implements FeatureValuesRetriever<NationalIdFeatureInputDto> {

  private final NationalIdDocumentQuery.Factory documentQueryFactory;

  @Override
  public NationalIdFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = documentQueryFactory.create(matchData);
    var inputBuilder = NationalIdFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyDocumentNumbers(toDistinctList(query.apNationalIds()));
      inputBuilder.watchlistDocumentNumbers(toDistinctList(query.mpNationalIds()));
    } else {
      inputBuilder.alertedPartyDocumentNumbers(emptyList());
      inputBuilder.watchlistDocumentNumbers(emptyList());
    }

    var result = inputBuilder
        .feature(getFeatureName())
        //FIXME mmrowka which country to choose if there is many IDs?
        //.alertedPartyCountry("AP")
        //.watchlistCountry("PL")
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
