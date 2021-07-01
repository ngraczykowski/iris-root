package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;
import com.silenteight.hsbc.datasource.extractors.country.NationalityCountryQueryFacade;
import com.silenteight.hsbc.datasource.extractors.country.OtherCountryQueryFacade;
import com.silenteight.hsbc.datasource.extractors.country.ResidencyCountryFeatureQueryFacade;
import com.silenteight.hsbc.datasource.extractors.document.DocumentExtractor;

import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectCountryMatchingAliases;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.collectNames;
import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.joinNameParts;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class WorldCheckIndividualsXmlNamesAndCountriesExtractor {

  private final MatchData matchData;
  private final NameInformationServiceClient nameInformationServiceClient;

  public Stream<String> extract() {
    var responses = extractNameInformation();
    var foreignAliases = extractForeignAliases(responses);
    var countries = extractCountries();

    var joinedNamesStream = extractJoinedNamesStream(responses);

    var countryMatchingAliasesStream =
        collectCountryMatchingAliases(foreignAliases, countries).stream();

    var mergedNames = mergeStreams(
        joinedNamesStream,
        countryMatchingAliasesStream);

    return collectNames(mergedNames);
  }

  private Stream<String> mergeStreams(
      Stream<String> joinedNamesStream, Stream<String> countryMatchingAliasesStream) {
    return concat(joinedNamesStream, countryMatchingAliasesStream);
  }

  private List<String> extractCountries() {
    var nationalIdsCountries = new DocumentExtractor()
        .convertMatchedPartyDocumentNumbers(matchData)
        .getNationalIdsCountries()
        .stream();

    var nationalityCountries =
        new NationalityCountryQueryFacade(matchData)
            .mpWorldCheckIndividualCountries();

    var residenceCountries =
        new ResidencyCountryFeatureQueryFacade(matchData)
            .worldCheckIndividualsResidencies();

    var otherCountries =
        new OtherCountryQueryFacade(matchData)
            .mpWorldCheckIndividualsOtherCountries();

    return StreamEx.of(nationalIdsCountries)
        .append(nationalityCountries)
        .append(residenceCountries)
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
    return matchData.getWorldCheckIndividuals().stream()
        .flatMap(WorldCheckIndividualsXmlNamesAndCountriesExtractor::extractWorldCheckListRecordIds)
        .filter(Objects::nonNull)
        .distinct()
        .map(listRecordId -> GetNameInformationRequestDto.builder()
            .watchlistUuid(listRecordId)
            .build())
        .map(nameInformationServiceClient::getNameInformation)
        .distinct()
        .collect(toList());
  }

  private Stream<String> extractJoinedNamesStream(List<GetNameInformationResponseDto> responses) {
    return responses.stream()
        .map(response -> joinNameParts(response.getFirstName(), response.getLastName()));
  }

  private static Stream<String> extractWorldCheckListRecordIds(
      WorldCheckIndividual worldCheckIndividual) {
    return of(worldCheckIndividual.getListRecordId());
  }
}
