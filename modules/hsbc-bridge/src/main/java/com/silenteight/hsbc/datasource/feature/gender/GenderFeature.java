package com.silenteight.hsbc.datasource.feature.gender;

import com.silenteight.hsbc.bridge.domain.IndividualComposite;
import com.silenteight.hsbc.bridge.domain.PrivateListIndividuals;
import com.silenteight.hsbc.bridge.domain.WorldCheckIndividuals;
import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.dto.gender.GenderFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.gender.GenderFeatureInputDto.GenderFeatureInputDtoBuilder;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import io.micrometer.core.instrument.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class GenderFeature implements FeatureValuesRetriever<GenderFeatureInputDto> {

  @Override
  public GenderFeatureInputDto retrieve(MatchRawData matchRawData) {
    GenderFeatureInputDtoBuilder genderFeatureInputDtoBuilder = GenderFeatureInputDto.builder()
        .feature(getFeatureName())
        .alertedPartyGenders(emptyList())
        .watchlistGenders(emptyList());

    if (matchRawData.isIndividual()) {
      var individualComposite = matchRawData.getIndividualComposite();

      var worldCheckIndividualsGenders =
          getWorldCheckIndividualsGenders(individualComposite.getWorldCheckIndividuals());
      var privateListIndividualsGenders =
          getPrivateListIndividualsGenders(individualComposite.getPrivateListIndividuals());

      var wlGenders = mergeLists(
          worldCheckIndividualsGenders,
          privateListIndividualsGenders);

      var apGender = getCustomerIndividualsGender(individualComposite);

      genderFeatureInputDtoBuilder
          .alertedPartyGenders(createValidGenderAlertPartyListForAgents(apGender))
          .watchlistGenders(createValidGenderWatchListForAgent(wlGenders));
    }

    return genderFeatureInputDtoBuilder.build();
  }

  @Override
  public Feature getFeature() {
    return Feature.GENDER;
  }

  private Optional<String> getCustomerIndividualsGender(IndividualComposite individualComposite) {
    return ofNullable(individualComposite.getCustomerIndividuals().getGender());
  }

  private List<String> mergeLists(
      Stream<String> worldCheckIndividualsGenders, Stream<String> privateListIndividualsGenders) {
    return Stream
        .concat(worldCheckIndividualsGenders, privateListIndividualsGenders)
        .collect(toList());
  }

  private Stream<String> getPrivateListIndividualsGenders(
      List<PrivateListIndividuals> privateListIndividuals) {
    return privateListIndividuals.stream()
        .map(PrivateListIndividuals::getGender)
        .filter(StringUtils::isNotEmpty);
  }

  private Stream<String> getWorldCheckIndividualsGenders(
      List<WorldCheckIndividuals> worldCheckIndividuals) {
    return worldCheckIndividuals.stream()
        .map(WorldCheckIndividuals::getGender)
        .filter(StringUtils::isNotEmpty);
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
