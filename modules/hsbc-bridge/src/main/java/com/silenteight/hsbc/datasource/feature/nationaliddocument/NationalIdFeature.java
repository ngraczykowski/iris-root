package com.silenteight.hsbc.datasource.feature.nationaliddocument;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class NationalIdFeature implements FeatureValuesRetriever<NationalIdFeatureInputDto> {

  private final NationalIdDocumentQuery.Factory documentQueryFactory;

  @Override
  public NationalIdFeatureInputDto retrieve(MatchData matchData) {
    var query = documentQueryFactory.create(matchData);
    var inputBuilder = NationalIdFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyDocumentNumbers(toDistinctList(query.apNationalIds()));
      inputBuilder.watchlistDocumentNumbers(toDistinctList(query.mpNationalIds()));
    } else {
      inputBuilder.alertedPartyDocumentNumbers(emptyList());
      inputBuilder.watchlistDocumentNumbers(emptyList());
    }

    return inputBuilder
        .feature(getFeature().getName())
        //FIXME mmrowka which country to choose if there is many IDs?
        //.alertedPartyCountry("AP")
        //.watchlistCountry("PL")
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.NATIONAL_ID_DOCUMENT;
  }
}
