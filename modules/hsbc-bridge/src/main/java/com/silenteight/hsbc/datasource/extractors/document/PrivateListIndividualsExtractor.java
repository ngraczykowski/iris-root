package com.silenteight.hsbc.datasource.extractors.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;
import com.silenteight.hsbc.datasource.extractors.common.SimpleRegexBasedExtractor;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@RequiredArgsConstructor
class PrivateListIndividualsExtractor {

  private static final Pattern EDQ_SUFFIX = Pattern.compile("^\\s*[^0-9]+\\s*$");
  private final Document document;

  void extract(List<PrivateListIndividual> privateListIndividuals) {
    privateListIndividuals.forEach(p -> {
      extractPassportNumber(p.getPassportNumber());
      extractNationalId(p.getNationalId());
      extractEdqDocument(p.getEdqDrivingLicence());
      extractEdqDocument(p.getEdqTaxNumber());
      extractEdqSuffix(p.getEdqSuffix());
    });
  }

  private void extractPassportNumber(String passportNumber) {
    if (isEmpty(passportNumber)) {
      return;
    }
    Stream.of(passportNumber.split("[,;]"))
        .map(PassportNumberFieldExtractor::new)
        .map(SimpleRegexBasedExtractor::extract)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .forEach(document::addPassportNumber);
  }

  private void extractNationalId(String nationalId) {
    if (isEmpty(nationalId)) {
      return;
    }
    Stream.of(nationalId.split(","))
        .forEach(document::addNationalIdNumber);
  }

  private void extractEdqDocument(String edqDocument) {
    if (isEmpty(edqDocument)) {
      return;
    }
    document.addOtherDocumentNumber(edqDocument);
  }

  private void extractEdqSuffix(String edqSuffix) {
    if (isEmpty(edqSuffix)) {
      return;
    }
    var matcher = EDQ_SUFFIX.matcher(edqSuffix);
    if (matcher.find()) {
      var suffix = matcher.group(0);
      document.addPassportNumber(suffix);
      document.addNationalIdNumber(suffix);
      document.addOtherDocumentNumber(suffix);
    }
  }
}
