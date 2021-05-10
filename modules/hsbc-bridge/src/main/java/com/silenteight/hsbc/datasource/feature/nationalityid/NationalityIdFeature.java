package com.silenteight.hsbc.datasource.feature.nationalityid;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

@RequiredArgsConstructor
public class NationalityIdFeature implements FeatureValuesRetriever<NationalIdFeatureInputDto> {

  private final AlertedPartyDocumentQuery.Factory alertedPartyDocumentQueryFactory;
  private final MatchedPartyDocumentQuery.Factory matchedPartyDocumentQueryFactory;

  @Override
  public NationalIdFeatureInputDto retrieve(MatchData matchData) {
    var nationalIdFeatureInputDtoBuilder =
        NationalIdFeatureInputDto.builder()
            //FIXME mmrowka which country to choose if there is many IDs?
            //.alertedPartyCountry("AP")
            //.watchlistCountry("PL")
            .feature(getFeatureName());

    if (matchData.isIndividual()) {
      var apDocumentQuery = alertedPartyDocumentQueryFactory.create(
          matchData.getCustomerIndividual());
      nationalIdFeatureInputDtoBuilder.alertedPartyDocumentNumbers(
          apDocumentQuery.allDocumentsNumbers());

      var mpDocumentQuery = matchedPartyDocumentQueryFactory.create(matchData);
      nationalIdFeatureInputDtoBuilder.watchlistDocumentNumbers(
          mpDocumentQuery.allDocumentsNumbers());
    }

    return nationalIdFeatureInputDtoBuilder.build();
  }

  @Override
  public Feature getFeature() {
    return Feature.NATIONALITY_ID;
  }
}
