package com.silenteight.hsbc.datasource.feature.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;


@RequiredArgsConstructor
public class NationalityCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  private final NationalityCountryQuery.Factory nationalityCountryQueryFactory;

  @Override
  public CountryFeatureInputDto retrieve(MatchRawData matchRawData) {
    var query = nationalityCountryQueryFactory.create(matchRawData.getIndividualComposite());

    var apIdDocumentCountry = query.apLine4DocumentCountry();
    var apFieldsCountries = query.apFieldsIndividualCountries();

    var mpDocument = query.mpDocumentCountries();
    var mpWorldCheckCountries = query.mpWorldCheckCountries();

    return CountryFeatureInputDto.builder()
        .feature(getFeature().getName())
        .alertedPartyCountries(concat(apIdDocumentCountry, apFieldsCountries).collect(toList()))
        .watchlistCountries(concat(mpDocument, mpWorldCheckCountries).collect(toList()))
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.NATIONALITY_COUNTRY;
  }
}
