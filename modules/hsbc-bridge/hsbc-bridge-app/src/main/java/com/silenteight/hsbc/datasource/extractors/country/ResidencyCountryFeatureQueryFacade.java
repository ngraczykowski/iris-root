package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.*;
import com.silenteight.hsbc.datasource.feature.country.ResidencyCountryFeatureQuery;

import org.apache.commons.lang3.StringUtils;

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
  public Stream<String> nnsIndividualsResidencies() {
    return individualComposite.getNnsIndividuals()
        .stream()
        .flatMap(ResidencyCountryFeatureQueryFacade::extractNnsIndividualsResidencies);
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
    return individualComposite.getCustomerIndividuals().stream()
        .flatMap(customerIndividual -> Stream.of(
            customerIndividual.getResidenceCountries(),
            customerIndividual.getAddressCountry(),
            customerIndividual.getCountryOfResidence(),
            customerIndividual.getEdqResidenceCountriesCode(),
            customerIndividual.getSourceCountry()))
        .filter(StringUtils::isNotBlank)
        .map(x -> x.replaceAll(INDIVIDUAL_RESIDENCIES_REGEX, "").trim());
  }

  private static Stream<String> extractWorldCheckResidencies(
      WorldCheckIndividual worldCheckIndividual) {
    return Stream.of(
        worldCheckIndividual.getAddressCountry(),
        worldCheckIndividual.getResidencyCountry()
    ).filter(StringUtils::isNotBlank);
  }

  private static Stream<String> extractNnsIndividualsResidencies(
      NegativeNewsScreeningIndividuals nnsIndividuals) {
    return Stream.of(
        nnsIndividuals.getAddressCountry(),
        nnsIndividuals.getResidenceCountry()
    ).filter(StringUtils::isNotBlank);
  }

  private static Stream<String> extractPrivateListIndividualsResidencies(
      PrivateListIndividual privateListIndividual) {
    return Stream.of(
        privateListIndividual.getAddressCountry(),
        privateListIndividual.getResidencyCountry()
    ).filter(StringUtils::isNotBlank);
  }

  private static Stream<String> extractCtrpScreeningResidencies(
      CtrpScreening ctrpScreeningResidencies) {
    return Stream.of(
        ctrpScreeningResidencies.getCountryName(),
        ctrpScreeningResidencies.getCountryCode(),
        ctrpScreeningResidencies.getCtrpValue()
    ).filter(StringUtils::isNotBlank);
  }
}
