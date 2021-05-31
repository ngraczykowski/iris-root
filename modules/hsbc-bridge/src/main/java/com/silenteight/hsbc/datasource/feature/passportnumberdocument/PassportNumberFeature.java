package com.silenteight.hsbc.datasource.feature.passportnumberdocument;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.document.DocumentFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class PassportNumberFeature implements FeatureValuesRetriever<DocumentFeatureInputDto> {

  private final PassportNumberDocumentQuery.Factory documentQueryFactory;

  @Override
  public DocumentFeatureInputDto retrieve(MatchData matchData) {
    var query = documentQueryFactory.create(matchData);
    var inputBuilder = DocumentFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyDocuments(toDistinctList(query.apPassportNumbers()));
      inputBuilder.watchlistDocuments(toDistinctList(query.mpPassportNumbers()));
    } else {
      inputBuilder.alertedPartyDocuments(emptyList());
      inputBuilder.watchlistDocuments(emptyList());
    }

    return inputBuilder
        .feature(getFeatureName())
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.PASSPORT_NUMBER_DOCUMENT;
  }
}
