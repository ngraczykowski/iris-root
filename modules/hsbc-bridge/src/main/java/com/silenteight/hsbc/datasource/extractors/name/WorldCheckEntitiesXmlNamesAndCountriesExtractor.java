package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;
import com.silenteight.hsbc.datasource.extractors.country.OtherCountryQueryFacade;
import com.silenteight.hsbc.datasource.feature.incorporationcountry.IncorporationCountryFeature;
import com.silenteight.hsbc.datasource.feature.registrationcountry.RegistrationCountryFeature;

import one.util.streamex.StreamEx;

import java.util.*;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectCountryMatchingAliases;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class WorldCheckEntitiesXmlNamesAndCountriesExtractor {

  private final MatchData matchData;
  private final NameInformationServiceClient nameInformationServiceClient;

  public Stream<String> extract() {
    var responses = extractNameInformation();
    var foreignAliases = extractForeignAliases(responses);
    var countries = extractCountries();

    var lastNamesStream = extractLastNamesStream(responses);

    var countryMatchingAliasesStream =
        collectCountryMatchingAliases(foreignAliases, countries).stream();

    var mergedNames = mergeStreams(
        lastNamesStream,
        countryMatchingAliasesStream);

    return collectNames(mergedNames);
  }

  private Stream<String> mergeStreams(
      Stream<String> lastNamesStream, Stream<String> countryMatchingAliasesStream) {
    return concat(lastNamesStream, countryMatchingAliasesStream);
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
        .collect(toList());
  }

  private List<ForeignAliasDto> extractForeignAliases(
      List<GetNameInformationResponseDto> responses) {
    return responses.stream()
        .map(GetNameInformationResponseDto::getForeignAliases)
        .flatMap(Collection::stream)
        .collect(toList());
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
        .collect(toList());
  }

  private Stream<String> extractLastNamesStream(List<GetNameInformationResponseDto> responses) {
    return responses.stream().map(GetNameInformationResponseDto::getLastName);
  }

  private static Stream<String> extractWorldCheckListRecordIds(WorldCheckEntity worldCheckEntity) {
    return of(worldCheckEntity.getListRecordId());
  }
}
