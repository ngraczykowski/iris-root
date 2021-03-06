package com.silenteight.hsbc.datasource.feature.date;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class ApDateExtractor implements Extractor {

  private final List<CustomerIndividual> customerIndividuals;

  Stream<String> extract() {
    var apDobs = customerIndividuals.stream()
        .flatMap(customerIndividual -> Stream.of(
            customerIndividual.getDateOfBirth(),
            customerIndividual.getBirthDate(),
            customerIndividual.getYearOfBirth()))
        .filter(StringUtils::isNotBlank)
        .distinct();

    return result(apDobs);
  }
}
