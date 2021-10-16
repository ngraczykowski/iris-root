package com.silenteight.payments.bridge.etl.processing.model.firco;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

import java.util.List;
import java.util.stream.Stream;

@Value
@Builder
public class FircoWatchlistParty {

  String id;
  boolean isException;
  List<String> officialReferences;
  List<String> names;
  List<Address> addresses;
  String origin;
  String designation;
  List<String> keywords;
  WatchlistType type;
  List<Code> codes;
  String additionalInfo;
  List<String> datesOfBirth;
  List<String> placesOfBirth;
  List<String> hyperlinks;
  String userData1;
  String userData2;
  String nationality;
  boolean isPep;
  boolean isFep;

  Stream<String> findAllNationalIdCodes() {
    return codes.stream().filter(code -> "NationalID".equals(code.getType())).map(Code::getName);
  }

  Stream<String> findAllPassportCodes() {
    return codes.stream().filter(code -> "Passport".equals(code.getType())).map(Code::getName);
  }

  Stream<String> findAllSearchCodeCodes() {
    return codes.stream().filter(code -> "SearchCode".equals(code.getType())).map(Code::getName);
  }

  Stream<String> findAllBicCodes() {
    return codes.stream().filter(code -> "Bic".equals(code.getType())).map(Code::getName);
  }

  @Value
  @Builder
  public static class Address {

    String isMain;
    String postalAddress;
    List<String> cities;
    List<String> states;
    List<String> countries;
  }

  @Value
  public static class Code {

    String name;
    String type;
  }
}
