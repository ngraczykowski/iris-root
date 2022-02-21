package com.silenteight.payments.bridge.etl.processing.model.firco;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

import java.util.List;

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
