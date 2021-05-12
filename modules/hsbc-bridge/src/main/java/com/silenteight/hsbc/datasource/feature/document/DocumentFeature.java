package com.silenteight.hsbc.datasource.feature.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.document.DocumentFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class DocumentFeature implements FeatureValuesRetriever<DocumentFeatureInputDto> {

  private final DocumentQuery.Factory documentQueryFactory;

  @Override
  public DocumentFeatureInputDto retrieve(MatchData matchData) {
    var query = documentQueryFactory.create(matchData);
    var inputBuilder = DocumentFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyDocuments(toDistinctList(query.apDocumentNumbers()));
      inputBuilder.watchlistDocuments(
          toDistinctList(query.mpDocumentNumbers(), query.mpEdqTaxNumber()));
    } else {
      inputBuilder.alertedPartyDocuments(emptyList());
      inputBuilder.watchlistDocuments(emptyList());
    }

    return inputBuilder
        .feature(getFeature().getName())
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.DOCUMENT;
  }
}
