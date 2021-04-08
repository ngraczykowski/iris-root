package com.silenteight.hsbc.datasource.feature.converter;

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

  private List<String> passportNumbers = new ArrayList<>();
  private List<String> nationalIdNumbers = new ArrayList<>();
  private List<String> otherDocumentNumbers = new ArrayList<>();

  public void addOtherDocumentNumber(String documentNumber) {
    this.otherDocumentNumbers.add(documentNumber);
  }

  public void addPassportNumber(String passportNumber) {
    this.passportNumbers.add(passportNumber);
  }

  public void addNationalIdNumber(String nationalIdNumber) {
    this.nationalIdNumbers.add(nationalIdNumber);
  }

  public List<String> getAllDocumentsNumbers() {
    return Stream.of(passportNumbers, nationalIdNumbers, otherDocumentNumbers)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
