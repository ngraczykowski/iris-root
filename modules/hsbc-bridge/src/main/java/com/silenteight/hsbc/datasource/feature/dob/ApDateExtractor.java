package com.silenteight.hsbc.datasource.feature.dob;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class ApDateExtractor implements Extractor {

  private static final DobDeviationsFilter DOB_DEVIATIONS_FILTER = new DobDeviationsFilter();
  private static final DobDateFilter DOB_DATE_FILTER = new DobDateFilter();
  private static final DateExtractor DATE_EXTRACTOR = new DateExtractor();

  private final List<CustomerIndividual> customerIndividuals;

  Stream<String> extract() {
    var apDobs = customerIndividuals.stream()
        .flatMap(customerIndividual -> Stream.of(
            customerIndividual.getDateOfBirth(),
            customerIndividual.getBirthDate(),
            customerIndividual.getYearOfBirth()))
        .filter(StringUtils::isNotBlank)
        .filter(DOB_DEVIATIONS_FILTER)
        .filter(DOB_DATE_FILTER)
        .map(DATE_EXTRACTOR)
        .distinct();

    return result(apDobs);
  }
}
