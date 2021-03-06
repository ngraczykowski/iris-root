package com.silenteight.hsbc.datasource.extractors.document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Setter(AccessLevel.NONE)
public class Document {

  private final List<String> passportNumbers = new ArrayList<>();
  private final List<String> nationalIds = new ArrayList<>();
  private final List<String> otherDocuments = new ArrayList<>();
  private final List<String> nationalIdsCountries = new ArrayList<>();
  private final List<String> passportCountries = new ArrayList<>();
  private final List<String> documentIdentificationCountries = new ArrayList<>();

  public void addOtherDocumentNumber(String documentNumber) {
    this.otherDocuments.add(documentNumber);
  }

  void addPassportNumber(String passportNumber) {
    this.passportNumbers.add(passportNumber);
  }

  void addNationalIdNumber(String nationalIdNumber) {
    this.nationalIds.add(nationalIdNumber);
  }

  public List<String> getAllDocumentsNumbers() {
    return Stream.of(passportNumbers, nationalIds, otherDocuments)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  void addNationalIdCountry(String nationalIdsCountry) {
    this.nationalIdsCountries.add(nationalIdsCountry);
  }

  void addPassportCountry(String passportCountry) {
    this.passportCountries.add(passportCountry);
  }

  void addDocumentIdentificationCountry(String country) {
    this.documentIdentificationCountries.add(country);
  }

  public List<String> getAllCountries() {
    return Stream.of(nationalIdsCountries, passportCountries, documentIdentificationCountries)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
