package com.silenteight.hsbc.datasource.feature.gender;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.*;
import com.silenteight.hsbc.datasource.dto.gender.GenderFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class GenderFeature implements FeatureValuesRetriever<GenderFeatureInputDto> {

  @Override
  public GenderFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var genderFeatureInputDtoBuilder = GenderFeatureInputDto.builder()
        .feature(getFeatureName())
        .alertedPartyGenders(Collections.emptyList())
        .watchlistGenders(Collections.emptyList());

    if (matchData.isIndividual()) {
      var worldCheckIndividualsGenders =
          getWorldCheckIndividualsGenders(matchData.getWorldCheckIndividuals());
      var privateListIndividualsGenders =
          getPrivateListIndividualsGenders(matchData.getPrivateListIndividuals());
      var nnsIndividualsGenders =
          getNnsIndividualsGenders(matchData.getNnsIndividuals());

      var wlGenders = mergeLists(
          worldCheckIndividualsGenders,
          privateListIndividualsGenders,
          nnsIndividualsGenders);

      var apGender = getCustomerIndividualsGender(matchData);

      genderFeatureInputDtoBuilder
          .alertedPartyGenders(createValidGenderAlertPartyListForAgents(apGender))
          .watchlistGenders(createValidGenderWatchListForAgent(wlGenders));
    }

    var result = genderFeatureInputDtoBuilder.build();

    log.debug(
        "Datasource response for feature: {} with alerted party size {} and watchlist party size {}.",
        result.getFeature(),
        result.getAlertedPartyGenders().size(),
        result.getWatchlistGenders().size());

    return result;
  }

  @Override
  public Feature getFeature() {
    return Feature.GENDER;
  }

  private Stream<String> getCustomerIndividualsGender(IndividualComposite individualComposite) {
    var customerIndividuals = individualComposite.getCustomerIndividuals();

    return customerIndividuals.stream()
        .map(GenderFieldsWrapper::fromCustomerIndividual)
        .map(GenderFieldsWrapper::tryExtracting)
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  private List<String> mergeLists(
      Stream<String> worldCheckIndividualsGenders, Stream<String> privateListIndividualsGenders,
      Stream<String> nnsIndividualsGenders) {
    return Stream
        .of(worldCheckIndividualsGenders, privateListIndividualsGenders, nnsIndividualsGenders)
        .flatMap(stream -> stream)
        .collect(Collectors.toList());
  }

  private Stream<String> getPrivateListIndividualsGenders(
      List<PrivateListIndividual> privateListIndividuals) {
    return privateListIndividuals.stream()
        .map(GenderFieldsWrapper::fromPrivateListIndividual)
        .map(GenderFieldsWrapper::tryExtracting)
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  private Stream<String> getWorldCheckIndividualsGenders(
      List<WorldCheckIndividual> worldCheckIndividuals) {
    return worldCheckIndividuals.stream()
        .map(GenderFieldsWrapper::fromWorldCheckIndividual)
        .map(GenderFieldsWrapper::tryExtracting)
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  private Stream<String> getNnsIndividualsGenders(
      List<NegativeNewsScreeningIndividuals> nnsIndividuals) {
    return nnsIndividuals.stream()
        .map(GenderFieldsWrapper::fromNnsIndividual)
        .map(GenderFieldsWrapper::tryExtracting)
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  private List<String> createValidGenderAlertPartyListForAgents(Stream<String> apGenders) {
    return apGenders.distinct().collect(Collectors.toList());
  }

  private List<String> createValidGenderWatchListForAgent(List<String> wlGenders) {
    if (containsContradictoryGenderValues(wlGenders)) {
      return wlGenders.stream()
          .findFirst()
          .stream()
          .collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  private boolean containsContradictoryGenderValues(List<String> wlGenders) {
    if (wlGenders.isEmpty()) {
      return false;
    }
    return wlGenders.stream().allMatch(gender -> gender.equals("M") || gender.equals("F"));
  }
}
