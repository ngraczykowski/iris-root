package com.silenteight.hsbc.datasource.feature.document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.document.DocumentFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;
import com.silenteight.hsbc.datasource.util.StreamUtils;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class PassportNumberFeature implements FeatureValuesRetriever<DocumentFeatureInputDto> {

  private final PassportNumberDocumentQuery.Factory documentQueryFactory;

  @Override
  public DocumentFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = documentQueryFactory.create(matchData);
    var inputBuilder = DocumentFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyDocuments(StreamUtils.toDistinctList(query.apPassportNumbers()));
      inputBuilder.watchlistDocuments(StreamUtils.toDistinctList(query.mpPassportNumbers()));
    } else {
      inputBuilder.alertedPartyDocuments(Collections.emptyList());
      inputBuilder.watchlistDocuments(Collections.emptyList());
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
    return Feature.PASSPORT_NUMBER_DOCUMENT;
  }
}
