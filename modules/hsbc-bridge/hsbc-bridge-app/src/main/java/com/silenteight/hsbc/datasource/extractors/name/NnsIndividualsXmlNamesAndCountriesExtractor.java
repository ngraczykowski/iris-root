/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;
import com.silenteight.hsbc.datasource.dto.name.ForeignAliasDto;
import com.silenteight.hsbc.datasource.dto.name.GetNameInformationRequestDto;
import com.silenteight.hsbc.datasource.dto.name.GetNameInformationResponseDto;
import com.silenteight.hsbc.datasource.extractors.country.NationalityCountryQueryFacade;
import com.silenteight.hsbc.datasource.extractors.country.OtherCountryQueryFacade;
import com.silenteight.hsbc.datasource.extractors.country.ResidencyCountryFeatureQueryFacade;
import com.silenteight.hsbc.datasource.extractors.document.DocumentExtractor;

import one.util.streamex.StreamEx;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class NnsIndividualsXmlNamesAndCountriesExtractor {

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
    var nationalIdsCountries =
        new DocumentExtractor()
            .convertMatchedPartyDocumentNumbers(matchData).getNationalIdsCountries().stream();

    var nationalityCountries =
        new NationalityCountryQueryFacade(matchData).nnsIndividualsCountries();

    var residenceCountries =
        new ResidencyCountryFeatureQueryFacade(matchData).nnsIndividualsResidencies();

    var otherCountries =
        new OtherCountryQueryFacade(matchData).nnsIndividualsOtherCountries();

    return StreamEx.of(nationalIdsCountries)
        .append(nationalityCountries)
        .append(residenceCountries)
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
    return matchData.getNnsIndividuals().stream()
        .flatMap(NnsIndividualsXmlNamesAndCountriesExtractor::extractNnsListRecordIds)
        .filter(Objects::nonNull)
        .distinct()
        .map(
            listRecordId ->
                GetNameInformationRequestDto.builder().watchlistUuid(listRecordId).build())
        .map(nameInformationServiceClient::getNameInformation)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .distinct()
        .collect(Collectors.toList());
  }

  private static Stream<String> extractNnsListRecordIds(
      NegativeNewsScreeningIndividuals nnsIndividual) {
    return Stream.of(nnsIndividual.getListRecordId());
  }
}
