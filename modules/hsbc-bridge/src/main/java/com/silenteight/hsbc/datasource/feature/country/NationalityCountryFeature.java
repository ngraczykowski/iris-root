package com.silenteight.hsbc.datasource.feature.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;


@RequiredArgsConstructor
public class NationalityCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  private final NationalityCountryQuery.Factory nationalityCountryQueryFactory;

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {
    var inputBuilder = CountryFeatureInputDto.builder()
        .feature(getFeatureName());

    if (matchData.isIndividual()) {
      var query = nationalityCountryQueryFactory.create(matchData);

      var apIdDocumentCountry = query.apLine4DocumentCountry();
      var apFieldsCountries = query.apFieldsIndividualCountries();

      var mpDocument = query.mpDocumentCountries();
      var mpWorldCheckCountries = query.getWatchlistIndividualsNationalityCountry();

      inputBuilder.alertedPartyCountries(
          concat(apIdDocumentCountry, apFieldsCountries).distinct().collect(toList()));
      inputBuilder.watchlistCountries(
          concat(mpDocument, mpWorldCheckCountries).distinct().collect(toList()));
    } else {
      inputBuilder.alertedPartyCountries(List.of());
      inputBuilder.watchlistCountries(List.of());
    }

    return inputBuilder.build();
  }

  @Override
  public Feature getFeature() {
    return Feature.NATIONALITY_COUNTRY;
  }
}
