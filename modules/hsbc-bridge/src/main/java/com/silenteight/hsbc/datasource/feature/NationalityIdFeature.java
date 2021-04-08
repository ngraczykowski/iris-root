package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.bridge.domain.IndividualComposite;
import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto.NationalIdFeatureInputDtoBuilder;
import com.silenteight.hsbc.datasource.feature.converter.Document;
import com.silenteight.hsbc.datasource.feature.converter.NationalIdFeatureConverter;

class NationalityIdFeature implements FeatureValuesRetriever<NationalIdFeatureInputDto> {

  @Override
  public NationalIdFeatureInputDto retrieve(MatchRawData matchRawData) {

    NationalIdFeatureConverter nationalIdFeatureConverter = new NationalIdFeatureConverter();
    NationalIdFeatureInputDtoBuilder nationalIdFeatureInputDtoBuilder =
        NationalIdFeatureInputDto.builder()
            //FIXME mmrowka which country to choose if there is many IDs?
            //.alertedPartyCountry("AP")
            //.watchlistCountry("PL")
            .feature(getFeatureName());

    if (matchRawData.isIndividual()) {
      IndividualComposite individualComposite = matchRawData.getIndividualComposite();

      Document alertedPartyDocument =
          nationalIdFeatureConverter.convertAlertedPartyDocumentNumbers(
              individualComposite.getCustomerIndividuals());

      nationalIdFeatureInputDtoBuilder.alertedPartyDocumentNumbers(
          alertedPartyDocument.getAllDocumentsNumbers());

      Document matchedPartyDocument =
          nationalIdFeatureConverter.convertMatchedPartyDocumentNumbers(individualComposite);

      nationalIdFeatureInputDtoBuilder.watchlistDocumentNumbers(
          matchedPartyDocument.getAllDocumentsNumbers());
    }

    return nationalIdFeatureInputDtoBuilder.build();
  }

  @Override
  public Feature getFeature() {
    return Feature.NATIONALITY_ID;
  }
}
