package com.silenteight.hsbc.datasource.feature.nationalityid;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;

@RequiredArgsConstructor
public class NationalityIdFeature implements FeatureValuesRetriever<NationalIdFeatureInputDto> {

  private final AlertedPartyDocumentQuery.Factory alertedPartyDocumentQueryFactory;
  private final MatchedPartyDocumentQuery.Factory matchedPartyDocumentQueryFactory;

  @Override
  public NationalIdFeatureInputDto retrieve(MatchData matchData) {
    var inputBuilder = NationalIdFeatureInputDto.builder()
            //FIXME mmrowka which country to choose if there is many IDs?
            //.alertedPartyCountry("AP")
            //.watchlistCountry("PL")
            .feature(getFeatureName());

    if (matchData.isIndividual()) {
      var apDocumentQuery = alertedPartyDocumentQueryFactory.create(
          matchData.getCustomerIndividual());
      inputBuilder.alertedPartyDocumentNumbers(apDocumentQuery.allDocumentsNumbers());

      var mpDocumentQuery = matchedPartyDocumentQueryFactory.create(matchData);
      inputBuilder.watchlistDocumentNumbers(mpDocumentQuery.allDocumentsNumbers());
    } else {
      inputBuilder.alertedPartyDocumentNumbers(List.of());
      inputBuilder.watchlistDocumentNumbers(List.of());
    }

    return inputBuilder.build();
  }

  @Override
  public Feature getFeature() {
    return Feature.NATIONALITY_ID;
  }
}
