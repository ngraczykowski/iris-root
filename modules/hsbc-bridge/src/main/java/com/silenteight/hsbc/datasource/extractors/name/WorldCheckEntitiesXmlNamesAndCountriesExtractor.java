package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;
import com.silenteight.hsbc.datasource.dto.name.ForeignAliasDto;
import com.silenteight.hsbc.datasource.dto.name.GetNameInformationRequestDto;
import com.silenteight.hsbc.datasource.dto.name.GetNameInformationResponseDto;
import com.silenteight.hsbc.datasource.extractors.country.OtherCountryQueryFacade;
import com.silenteight.hsbc.datasource.feature.country.IncorporationCountryFeature;
import com.silenteight.hsbc.datasource.feature.country.RegistrationCountryFeature;

import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckEntitiesXmlNamesAndCountriesExtractor {

  private final MatchData matchData;
  private final NameInformationServiceClient nameInformationServiceClient;

  public Stream<String> extract() {
    var responses = extractNameInformation();
    var foreignAliases = extractForeignAliases(responses);
    var countries = extractCountries();
    var countryMatchingAliasesStream =
        NameExtractor.collectCountryMatchingAliases(foreignAliases, countries).stream();

    return NameExtractor.collectNames(countryMatchingAliasesStream);
  }

  private List<String> extractCountries() {

    var incorporationCountries = IncorporationCountryFeature
        .worldCheckEntitiesIncorporationCountries(matchData.getWorldCheckEntities());

    var registrationCountries = RegistrationCountryFeature
        .worldCheckEntityCountries(matchData.getWorldCheckEntities());

    var otherCountries = new OtherCountryQueryFacade(matchData)
        .mpWorldCheckEntitiesOtherCountries();

    return StreamEx.of(incorporationCountries)
        .append(registrationCountries)
        .append(otherCountries)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private List<ForeignAliasDto> extractForeignAliases(
      List<GetNameInformationResponseDto> responses) {
    return responses.stream()
        .map(GetNameInformationResponseDto::getForeignAliases)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  private List<GetNameInformationResponseDto> extractNameInformation() {
    return matchData.getWorldCheckEntities().stream()
        .flatMap(WorldCheckEntitiesXmlNamesAndCountriesExtractor::extractWorldCheckListRecordIds)
        .filter(Objects::nonNull)
        .distinct()
        .map(listRecordId -> GetNameInformationRequestDto.builder()
            .watchlistUuid(listRecordId)
            .build())
        .map(nameInformationServiceClient::getNameInformation)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .distinct()
        .collect(Collectors.toList());
  }

  private static Stream<String> extractWorldCheckListRecordIds(WorldCheckEntity worldCheckEntity) {
    return Stream.of(worldCheckEntity.getListRecordId());
  }
}
