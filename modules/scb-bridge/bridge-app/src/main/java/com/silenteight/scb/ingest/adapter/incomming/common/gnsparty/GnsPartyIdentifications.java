package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import lombok.Builder;
import lombok.Value;

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Optional.ofNullable;

@Value
@Builder
public class GnsPartyIdentifications {

  private static final Set<Pattern> NATIONAL_ID_PATTERNS = IdentificationPattern.compileAll(
      IdentificationPattern.builder().pattern("(resident|national) (identity|id) card").build(),
      IdentificationPattern.builder().pattern("aadhaar").build(),
      IdentificationPattern.builder().pattern("local identity document").build(),
      IdentificationPattern.builder().pattern("taxpayer identification number").build(),
      IdentificationPattern.builder().pattern("PAN").caseSensitive(true).build(),
      IdentificationPattern.builder().pattern("NRIC").caseSensitive(true).build(),
      IdentificationPattern.builder().pattern("new nic number").build(),
      IdentificationPattern.builder().pattern("national identity card").build(),
      IdentificationPattern.builder().pattern("National Identification Card").build(),
      IdentificationPattern.builder().pattern("nid").build(),
      IdentificationPattern.builder().pattern("national id").build()
  );

  private static final Set<Pattern> PASSPORT_PATTERNS = IdentificationPattern.compileAll(
      IdentificationPattern.builder().pattern("passport").build(),
      IdentificationPattern.builder().pattern("valid travel document").build()
  );

  @Nullable
  String identificationType1;
  @Nullable
  String identificationNumber1;
  @Nullable
  String identificationType2;
  @Nullable
  String identificationNumber2;
  @Nullable
  String identificationType3;
  @Nullable
  String identificationNumber3;
  @Nullable
  String identificationType4;
  @Nullable
  String identificationNumber4;
  @Nullable
  String identificationTypeRest;
  @Nullable
  String identificationNumberRest;

  public static GnsPartyIdentifications fromGnsParty(GnsParty party) {
    var builder = GnsPartyIdentifications.builder();
    party.getStringField("identificationType1").ifPresent(builder::identificationType1);
    party.getStringField("identificationType2").ifPresent(builder::identificationType2);
    party.getStringField("identificationType3").ifPresent(builder::identificationType3);
    party.getStringField("identificationType4").ifPresent(builder::identificationType4);
    party.getStringField("identificationTypeRest").ifPresent(builder::identificationTypeRest);
    party.getStringField("identificationNumber1").ifPresent(builder::identificationNumber1);
    party.getStringField("identificationNumber2").ifPresent(builder::identificationNumber2);
    party.getStringField("identificationNumber3").ifPresent(builder::identificationNumber3);
    party.getStringField("identificationNumber4").ifPresent(builder::identificationNumber4);
    party.getStringField("identificationNumberRest").ifPresent(builder::identificationNumberRest);
    return builder.build();
  }

  public static GnsPartyIdentifications fromScreenableData(ScreenableData screenableData) {
    return GnsPartyIdentifications.builder()
        .identificationType1(screenableData.getIdentificationType1())
        .identificationNumber1(screenableData.getIdentificationNumber1())
        .identificationType2(screenableData.getIdentificationType2())
        .identificationNumber2(screenableData.getIdentificationNumber2())
        .identificationType3(screenableData.getIdentificationType3())
        .identificationNumber3(screenableData.getIdentificationNumber3())
        .identificationType4(screenableData.getIdentificationType4())
        .identificationNumber4(screenableData.getIdentificationNumber4())
        .identificationTypeRest(screenableData.getIdentificationTypeRest())
        .identificationNumberRest(screenableData.getIdentificationNumberRest())
        .build();
  }

  public List<String> collectPassportNumbers() {
    return collectIdentificationsMatching(PASSPORT_PATTERNS);
  }

  @Nonnull
  private List<String> collectIdentificationsMatching(Set<Pattern> patterns) {
    return getIdentificationMap()
        .entrySet()
        .stream()
        .filter(e -> anyPatternMatches(patterns, e.getKey()))
        .map(Entry::getValue)
        .collect(Collectors.toList());
  }

  private static boolean anyPatternMatches(Set<Pattern> patterns, String key) {
    return patterns.stream().anyMatch(p -> p.matcher(key).matches());
  }

  @Nonnull
  private Map<String, String> getIdentificationMap() {
    var map = new HashMap<String, String>();
    addIdentificationEntry(map, identificationType1, identificationNumber1);
    addIdentificationEntry(map, identificationType2, identificationNumber2);
    addIdentificationEntry(map, identificationType3, identificationNumber3);
    addIdentificationEntry(map, identificationType4, identificationNumber4);
    addIdentificationEntry(map, identificationTypeRest, identificationNumberRest);
    return map;
  }

  private static void addIdentificationEntry(Map<String, String> map, String type, String number) {
    ofNullable(type).ifPresent(k -> ofNullable(number).ifPresent(v -> map.put(k, v)));
  }

  public List<String> collectNationalIds() {
    return collectIdentificationsMatching(NATIONAL_ID_PATTERNS);
  }
}
