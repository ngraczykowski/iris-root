package com.silenteight.hsbc.datasource.feature.nationalityid;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.domain.IndividualComposite;
import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

@RequiredArgsConstructor
public class NationalityIdFeature implements FeatureValuesRetriever<NationalIdFeatureInputDto> {

  private final AlertedPartyDocumentQuery.Factory alertedPartyDocumentQueryFactory;
  private final MatchedPartyDocumentQuery.Factory matchedPartyDocumentQueryFactory;

  @Override
  public NationalIdFeatureInputDto retrieve(MatchRawData matchRawData) {
    var nationalIdFeatureInputDtoBuilder =
        NationalIdFeatureInputDto.builder()
            //FIXME mmrowka which country to choose if there is many IDs?
            //.alertedPartyCountry("AP")
            //.watchlistCountry("PL")
            .feature(getFeatureName());

    if (matchRawData.isIndividual()) {
      IndividualComposite individualComposite = matchRawData.getIndividualComposite();

      var apDocumentQuery = alertedPartyDocumentQueryFactory.create(
          individualComposite.getCustomerIndividuals());
      nationalIdFeatureInputDtoBuilder.alertedPartyDocumentNumbers(
          apDocumentQuery.allDocumentsNumbers());

      var mpDocumentQuery = matchedPartyDocumentQueryFactory.create(
          individualComposite);
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
