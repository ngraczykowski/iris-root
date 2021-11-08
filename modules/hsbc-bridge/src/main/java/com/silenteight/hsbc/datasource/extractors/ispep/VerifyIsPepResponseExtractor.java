package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class VerifyIsPepResponseExtractor {

  private final IndividualComposite individualComposite;
  private final IsPepServiceClient isPepServiceClient;

  public List<IsPepResponseDto> extract(
      List<String> apFields, GetModelFieldNamesResponseDto modelFieldNamesResponse) {

    var apFieldsMap = createApFieldsMap(apFields);

    return extractIsPepRequestToResponse(apFieldsMap, modelFieldNamesResponse);
  }

  private Map<String, String> createApFieldsMap(List<String> apFields) {
    var map = new HashMap<String, String>();
    var lobCountry = apFields.stream().findFirst();
    lobCountry.ifPresent(s -> map.put("CustomerIndividuals.edqLoBCountryCode", s));
    return map;
  }

  private List<IsPepResponseDto> extractIsPepRequestToResponse(
      Map<String, String> apFieldsMap, GetModelFieldNamesResponseDto modelFieldNamesResponse) {
    return individualComposite.getWorldCheckIndividuals().stream()
        .flatMap(VerifyIsPepResponseExtractor::extractWorldCheckListRecordIds)
        .filter(Objects::nonNull)
        .distinct()
        .map(listRecordId -> IsPepRequestDto.builder()
            .fieldNames(modelFieldNamesResponse.getFieldNames())
            .apFields(apFieldsMap)
            .bankRegion(modelFieldNamesResponse.getRegionName())
            .uid(listRecordId)
            .build())
        .map(isPepServiceClient::verifyIfIsPep)
        .distinct()
        .collect(Collectors.toList());
  }

  private static Stream<String> extractWorldCheckListRecordIds(
      WorldCheckIndividual worldCheckIndividual) {
    return Stream.of(worldCheckIndividual.getListRecordId());
  }
}
