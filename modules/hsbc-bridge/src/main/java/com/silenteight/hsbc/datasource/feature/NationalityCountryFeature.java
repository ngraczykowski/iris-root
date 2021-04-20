package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.bridge.domain.WorldCheckIndividuals;
import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.converter.NationalIdFeatureConverter;
import com.silenteight.hsbc.datasource.feature.converter.country.CustomerIndividualCountriesExtractor;
import com.silenteight.hsbc.datasource.feature.converter.country.IdentificationDocumentLine4CountryExtractor;
import com.silenteight.hsbc.datasource.feature.converter.country.WorldCheckIndividualsCountriesExtractor;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;


public class NationalityCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  @Override
  public CountryFeatureInputDto retrieve(MatchRawData matchRawData) {

    var apIdDocumentCountry = extractApLine4DocumentCountry(matchRawData);
    var apFieldsCountries = extractApFieldsIndividualCountries(matchRawData);

    var mpDocument = extractMpDocumentCountries(matchRawData);
    var mpWorldCheckCountries = extractMpCountriesFromWorldCheckIndividuals(
        matchRawData.getIndividualComposite().getWorldCheckIndividuals());

    return CountryFeatureInputDto.builder()
        .feature(Feature.NATIONALITY_COUNTRY.getName())
        .alertedPartyCountries(concat(apIdDocumentCountry, apFieldsCountries).collect(toList()))
        .watchlistCountries(concat(
            mpDocument, mpWorldCheckCountries).collect(toList()))
        .build();
  }

  private static Stream<String> extractMpDocumentCountries(MatchRawData matchRawData) {
    return new NationalIdFeatureConverter()
        .convertMatchedPartyDocumentNumbers(matchRawData.getIndividualComposite())
        .getAllCountries().stream();
  }

  private static Stream<String> extractApFieldsIndividualCountries(MatchRawData matchRawData) {
    return new CustomerIndividualCountriesExtractor(
        matchRawData.getIndividualComposite().getCustomerIndividuals()
    ).extract();
  }

  private Stream<String> extractApLine4DocumentCountry(MatchRawData matchRawData) {
    var documentFieldWithPossibleCountry = matchRawData.getIndividualComposite()
        .getCustomerIndividuals()
        .getIdentificationDocument4();

    return new IdentificationDocumentLine4CountryExtractor(
        documentFieldWithPossibleCountry).extract().stream();
  }

  private static Stream<String> extractMpCountriesFromWorldCheckIndividuals(
      List<WorldCheckIndividuals> worldCheckIndividuals) {
    return worldCheckIndividuals.stream()
        .map(WorldCheckIndividualsCountriesExtractor::new)
        .flatMap(WorldCheckIndividualsCountriesExtractor::extract);
  }

  @Override
  public Feature getFeature() {
    return Feature.NATIONALITY_COUNTRY;
  }
}
