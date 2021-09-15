package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening;
import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;
import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;
import com.silenteight.hsbc.datasource.feature.country.ResidencyCountryFeatureQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class ResidencyCountryFeatureQueryFacade implements ResidencyCountryFeatureQuery {

  private final IndividualComposite individualComposite;
  private static final String INDIVIDUAL_RESIDENCIES_REGEX = "([^a-zA-Z0-9' ]+)";

  @Override
  public Stream<String> worldCheckIndividualsResidencies() {
    return individualComposite.getWorldCheckIndividuals()
        .stream()
        .flatMap(ResidencyCountryFeatureQueryFacade::extractWorldCheckResidencies);
  }

  @Override
  public Stream<String> privateListIndividualsResidencies() {
    return individualComposite.getPrivateListIndividuals()
        .stream()
        .flatMap(ResidencyCountryFeatureQueryFacade::extractPrivateListIndividualsResidencies);
  }

  @Override
  public Stream<String> ctrpScreeningResidencies() {
    return individualComposite.getCtrpScreeningIndividuals()
        .stream()
        .flatMap(ResidencyCountryFeatureQueryFacade::extractCtrpScreeningResidencies);
  }

  @Override
  public Stream<String> customerIndividualResidencies() {
    var customerIndividuals = individualComposite.getCustomerIndividual();

    return Stream.of(
            customerIndividuals.getResidenceCountries(),
            customerIndividuals.getAddressCountry(),
            customerIndividuals.getCountryOfResidence(),
            customerIndividuals.getEdqResidenceCountriesCode(),
            customerIndividuals.getSourceCountry()
        )
        .map(x -> x.replaceAll(INDIVIDUAL_RESIDENCIES_REGEX, "").trim());
  }

  private static Stream<String> extractWorldCheckResidencies(
      WorldCheckIndividual worldCheckIndividual) {
    return Stream.of(
        worldCheckIndividual.getAddressCountry(),
        worldCheckIndividual.getResidencyCountry()
    );
  }

  private static Stream<String> extractPrivateListIndividualsResidencies(
      PrivateListIndividual privateListIndividual) {
    return Stream.of(
        privateListIndividual.getAddressCountry(),
        privateListIndividual.getResidencyCountry()
    );
  }

  private static Stream<String> extractCtrpScreeningResidencies(
      CtrpScreening ctrpScreeningResidencies) {
    return Stream.of(
        ctrpScreeningResidencies.getCountryName(),
        ctrpScreeningResidencies.getCountryCode(),
        ctrpScreeningResidencies.getCtrpValue()
    );
  }
}
