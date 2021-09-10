package com.silenteight.hsbc.datasource.feature.gender;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;
import com.silenteight.hsbc.datasource.dto.gender.GenderFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Slf4j
public class GenderFeature implements FeatureValuesRetriever<GenderFeatureInputDto> {

  @Override
  public GenderFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var genderFeatureInputDtoBuilder = GenderFeatureInputDto.builder()
        .feature(getFeatureName())
        .alertedPartyGenders(emptyList())
        .watchlistGenders(emptyList());

    if (matchData.isIndividual()) {
      var worldCheckIndividualsGenders =
          getWorldCheckIndividualsGenders(matchData.getWorldCheckIndividuals());
      var privateListIndividualsGenders =
          getPrivateListIndividualsGenders(matchData.getPrivateListIndividuals());

      var wlGenders = mergeLists(
          worldCheckIndividualsGenders,
          privateListIndividualsGenders);

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

  private Optional<String> getCustomerIndividualsGender(IndividualComposite individualComposite) {
    var customerIndividual = individualComposite.getCustomerIndividual();

    return GenderFieldsWrapper.fromCustomerIndividual(customerIndividual).tryExtracting();
  }

  private List<String> mergeLists(
      Stream<String> worldCheckIndividualsGenders, Stream<String> privateListIndividualsGenders) {
    return Stream
        .concat(worldCheckIndividualsGenders, privateListIndividualsGenders)
        .collect(toList());
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

  private List<String> createValidGenderAlertPartyListForAgents(Optional<String> apGender) {
    return apGender.map(List::of).orElseGet(Collections::emptyList);
  }

  private List<String> createValidGenderWatchListForAgent(List<String> wlGenders) {
    if (containsContradictoryGenderValues(wlGenders)) {
      return wlGenders.stream()
          .findFirst()
          .stream()
          .collect(toList());
    } else {
      return emptyList();
    }
  }

  private boolean containsContradictoryGenderValues(List<String> wlGenders) {
    if (wlGenders.isEmpty()) {
      return false;
    }
    return wlGenders.stream().allMatch(gender -> gender.equals("M") || gender.equals("F"));
  }
}
