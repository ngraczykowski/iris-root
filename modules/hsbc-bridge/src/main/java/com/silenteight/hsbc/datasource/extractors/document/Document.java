package com.silenteight.hsbc.datasource.extractors.document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Data
@Setter(AccessLevel.NONE)
public class Document {

  private final List<String> passportNumbers = new ArrayList<>();
  private final List<String> nationalIdNumbers = new ArrayList<>();
  private final List<String> otherDocumentNumbers = new ArrayList<>();
  private final List<String> nationalIdsCountries = new ArrayList<>();
  private final List<String> passportCountries = new ArrayList<>();

  public void addOtherDocumentNumber(String documentNumber) {
    this.otherDocumentNumbers.add(documentNumber);
  }

  void addPassportNumber(String passportNumber) {
    this.passportNumbers.add(passportNumber);
  }

  void addNationalIdNumber(String nationalIdNumber) {
    this.nationalIdNumbers.add(nationalIdNumber);
  }

  public List<String> getAllDocumentsNumbers() {
    return Stream.of(passportNumbers, nationalIdNumbers, otherDocumentNumbers)
        .flatMap(Collection::stream)
        .collect(toList());
  }

  void addNationalIdCountry(String country) {
    this.nationalIdsCountries.add(country);
  }

  void addPassportCountry(String country) {
    this.passportCountries.add(country);
  }

  public List<String> getAllCountries() {
    return Stream.of(nationalIdsCountries, passportCountries)
        .flatMap(Collection::stream)
        .collect(toList());
  }
}
