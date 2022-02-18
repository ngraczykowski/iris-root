package com.silenteight.payments.bridge.firco.dto.input;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HitDtoTest {

  @ParameterizedTest
  @MethodSource("extractWlNameFunctionTestsDataProvider")
  void extractWlNameFunctionTests(
      List<String> hitNames, String synonymIndex, String solutionType, String expectedWlName) {

    HitDto hitDto = createHitDto(hitNames, synonymIndex, solutionType);
    String actualWlName = hitDto.extractWlName();
    assertEquals(expectedWlName, actualWlName);
  }



  private static Stream<Arguments> extractWlNameFunctionTestsDataProvider() {

    return Stream.of(
        Arguments.of(List.of("RN-EXPLORATION OOO"), "0", "1", "RN-EXPLORATION OOO"),
        Arguments.of(List.of("RN-EXPLORATION OOO", "DANFOSS A/S"), "1", "1", "DANFOSS A/S"),
        Arguments.of(List.of("RN-EXPLORATION OOO", "DANFOSS A/S"), "-1", "1", ""),
        Arguments.of(
            List.of("ABDUL BASEER, ABDUL QADEER BASIR"), "0", "1",
            "ABDUL BASEER, ABDUL QADEER BASIR"),
        Arguments.of(
            List.of("ABDUL QADER, ABDUL HAI HAZEM", "ABDUL BASEER, ABDUL QADEER BASIR"), "0", "1",
            "ABDUL QADER, ABDUL HAI HAZEM"),
        Arguments.of(null, null, "1", ""),
        Arguments.of(List.of(), null, "1", ""),
        Arguments.of(List.of(""), null, "1", ""),
        Arguments.of(null, "", "1", ""),
        Arguments.of(List.of(), "", "1", ""),
        Arguments.of(List.of(""), "", "1", ""),
        Arguments.of(null, "1", "1", ""),
        Arguments.of(List.of(), "1", "1", ""),
        Arguments.of(List.of(""), "1", "1", ""),
        Arguments.of(List.of("MORNING STAR TRADING & MANUFACTURING FZCO"), null, "1", ""),
        Arguments.of(
            List.of("MORNING STAR TRADING & MANUFACTURING FZCO"), "", "1",
            "MORNING STAR TRADING & MANUFACTURING FZCO"),
        Arguments.of(
            List.of("MORNING STAR TRADING & MANUFACTURING FZCO"), "0", "1",
            "MORNING STAR TRADING & MANUFACTURING FZCO"),
        Arguments.of(List.of("", "", "LIU, GUANGYU"), "2", "1", "LIU, GUANGYU"),
        Arguments.of(List.of("HASSAN, GUD MULLAH MOHAMMAD", "ABC", ""), "0", null, ""),
        Arguments.of(List.of("HASSAN, GUD MULLAH MOHAMMAD", "ABC", ""), "0", "-1", ""),
        Arguments.of(List.of("HASSAN, GUD MULLAH MOHAMMAD", "ABC", ""), "0", "0", ""),
        Arguments.of(
            List.of("HASSAN, GUD MULLAH MOHAMMAD", "ABC", ""), "0", "1",
            "HASSAN, GUD MULLAH MOHAMMAD"),
        Arguments.of(List.of("HASSAN, GUD MULLAH MOHAMMAD", "ABC", ""), "0", "2", ""),
        Arguments.of(List.of("HASSAN, GUD MULLAH MOHAMMAD", "ABC", ""), "0", "3", ""),
        Arguments.of(List.of("HASSAN, GUD MULLAH MOHAMMAD", "ABC", ""), "0", "4", ""),
        Arguments.of(List.of("HASSAN, GUD MULLAH MOHAMMAD", "ABC", ""), "0", "5", ""),
        Arguments.of(List.of("HASSAN, GUD MULLAH MOHAMMAD", "ABC", ""), "0", "6", ""));
  }

  private HitDto createHitDto(List<String> hitNames, String synonymIndex, String solutionType) {
    return HitDto.builder()
        .hittedEntity(
            HittedEntityDto.builder()
                .names(Optional
                    .ofNullable(hitNames)
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(NameDto::new)
                    .collect(Collectors.toList()))
                .build())
        .solutionType(solutionType)
        .synonymIndex(synonymIndex)
        .build();
  }
}
