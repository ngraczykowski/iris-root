package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HittedEntityDto implements Serializable {

  private static final long serialVersionUID = 7668869052958833725L;
  @JsonProperty("ID")
  String id; // "GSN0002899"
  @JsonProperty("IsException")
  String isException; // "0"
  @JsonProperty("OfficialReferences")
  List<RequestOfficialReferenceDto> officialReferences;
  @JsonProperty("Names")
  List<NameDto> names;
  @JsonProperty("Addresses")
  List<RequestAddressDto> addresses;
  @JsonProperty("Origin")
  String origin; // "SANCTIONSGROUP"
  @JsonProperty("Designation")
  String designation; // "XX-LISTPARTYNEX-OTH"
  @JsonProperty("Keywords")
  List<KeywordDto> keywords;
  @JsonProperty("Type")
  String type; // "C"
  @JsonProperty("Codes")
  List<HittedEntityCodeDto> codes;
  @JsonProperty("AdditionalInfo")
  String additionalInfo;
  // "This indicates the transaction is connected to a party or country sanctioned by the
  // United States. If that connection is not apparent from the transaction message, obtain from
  // our client the name of the sanctioned party or country involved, and the nature of that
  // party's or country's connection to the transaction. Then process or decline according to Group
  // sanctions procedures. Any transaction related to Iran must not be processed but must be
  // escalated to Group CMO or Group Trade as appropriate for onward referral to Group.",
  @JsonProperty("DatesOfBirth")
  List<DateOfBirthDto> datesOfBirth;
  @JsonProperty("PlacesOfBirth")
  List<PlaceOfBirthDto> placesOfBirth;
  @JsonProperty("Hyperlinks")
  List<HyperlinkDto> hyperLinks;
  @JsonProperty("UserData1")
  String userData1; // "RFM-TS.NS.OB-DENY UPDATE"
  @JsonProperty("UserData2")
  String userData2; // ""
  @JsonProperty("Nationality")
  String nationality; // ""
  @JsonProperty("IsPEP")
  String isPep; // "0"
  @JsonProperty("IsFEP")
  String isFep; // "0"
  @JsonProperty("HideOnlyIDs")
  List<HideOnlyIdDto> hideOnlyIDs;
  @JsonProperty("HideIDs")
  List<HideIdDto> hideIDs;
  @JsonProperty("HideOrigins")
  List<HideOriginDto> hideOrigins;
  @JsonProperty("HideInTags")
  List<HideInTagDto> hideInTags;
  @JsonProperty("HideUnits")
  List<HideUnitDto> hideUnits;
  @JsonProperty("HideSenderReceivers")
  List<HideSenderReceiverDto> hideSenderReceivers;
}
