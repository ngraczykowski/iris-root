package com.silenteight.hsbc.datasource.feature.dob;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class ApDateExtractor {

  private static final DobDeviationsFilter DOB_DEVIATIONS_FILTER = new DobDeviationsFilter();
  private static final DobDateFilter DOB_DATE_FILTER = new DobDateFilter();
  private static final DateExtractor DATE_EXTRACTOR = new DateExtractor();
  public static final int DETAILS_LENGTH_INDICATOR = 4;

  private final CustomerIndividual customerIndividual;

  Stream<String> extract() {
    var apDobs = Stream.of(
        customerIndividual.getBirthDate(),
        customerIndividual.getDateOfBirth()
    )
        .filter(DOB_DEVIATIONS_FILTER)
        .filter(DOB_DATE_FILTER)
        .map(DATE_EXTRACTOR)
        .distinct()
        .collect(toList());

    var apYob = customerIndividual.getYearOfBirth();

    if (apDobsHasDetails(apDobs)) {
      return apDobs.stream();
    } else {
      return Stream.concat(apDobs.stream(), Stream.of(apYob));
    }
  }

  private static boolean apDobsHasDetails(List<String> apDobs) {
    return apDobs.stream()
        .map(String::length)
        .anyMatch(length -> length > DETAILS_LENGTH_INDICATOR);
  }
}
