package com.silenteight.hsbc.datasource.feature.document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.document.DocumentFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
public class OtherDocumentFeature implements FeatureValuesRetriever<DocumentFeatureInputDto> {

  private final OtherDocumentQuery.Factory documentQueryFactory;

  @Override
  public DocumentFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = documentQueryFactory.create(matchData);
    var inputBuilder = DocumentFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyDocuments(toDistinctList(query.apOtherDocuments()));
      inputBuilder.watchlistDocuments(
          toDistinctList(query.mpOtherDocuments(), query.mpEdqTaxNumber()));
    } else {
      inputBuilder.alertedPartyDocuments(emptyList());
      inputBuilder.watchlistDocuments(emptyList());
    }

    var result = inputBuilder
        .feature(getFeatureName())
        .build();

    log.debug(
        "Datasource response for feature: {} with alerted party size {} and watchlist party size {}.",
        result.getFeature(),
        result.getAlertedPartyDocuments().size(),
        result.getWatchlistDocuments().size());

    return result;
  }

  @Override
  public Feature getFeature() {
    return Feature.OTHER_DOCUMENT;
  }
}
