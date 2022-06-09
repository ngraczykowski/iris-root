/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.document;

import com.silenteight.hsbc.datasource.extractors.common.SimpleRegexBasedExtractor;
import com.silenteight.hsbc.datasource.extractors.country.NationalIdNumberFieldCountryExtractor;
import com.silenteight.hsbc.datasource.extractors.country.PassportNumberFieldCountryExtractor;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class IndividualDocumentExtractor {

  static final Pattern DOCUMENT_ID_PATTERN = Pattern.compile(".*ID$");
  static final Pattern OTHER_DOCUMENT_ID_PATTERN = Pattern.compile(".*(?<!ID)$");
  Document document;

  IndividualDocumentExtractor(Document document) {
    this.document = document;
  }

  void extractIdNumbers(String idNumbers) {
    if (StringUtils.isEmpty(idNumbers)) {
      return;
    }

    var splitIdNumbers = idNumbers.split("\\|");

    Stream.of(splitIdNumbers)
        .map(NationalIdNumberFieldCountryExtractor::new)
        .map(SimpleRegexBasedExtractor::extract)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .distinct()
        .forEach(document::addNationalIdCountry);

    var extractedIdNumbers =
        Stream.of(splitIdNumbers)
            .map(IdNumberFieldExtractor::new)
            .map(SimpleRegexBasedExtractor::extract)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

    extractedIdNumbers.stream()
        .filter(i -> DOCUMENT_ID_PATTERN.matcher(i).find())
        .forEach(document::addNationalIdNumber);

    extractedIdNumbers.stream()
        .filter(i -> OTHER_DOCUMENT_ID_PATTERN.matcher(i).find())
        .forEach(document::addOtherDocumentNumber);
  }

  void extractPassportNumbers(String passportNumber) {
    if (StringUtils.isEmpty(passportNumber)) {
      return;
    }

    var splitPassportNumbers = passportNumber.split(";");

    Stream.of(splitPassportNumbers)
        .map(PassportNumberFieldExtractor::new)
        .map(SimpleRegexBasedExtractor::extract)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .forEach(document::addPassportNumber);
  }

  void extractPassportCountries(String passportCountry) {
    if (StringUtils.isEmpty(passportCountry)) {
      return;
    }
    var splitPassportNumbers = passportCountry.split(";");

    Stream.of(splitPassportNumbers)
        .map(PassportNumberFieldCountryExtractor::new)
        .map(SimpleRegexBasedExtractor::extract)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .distinct()
        .forEach(document::addPassportCountry);
  }
}
