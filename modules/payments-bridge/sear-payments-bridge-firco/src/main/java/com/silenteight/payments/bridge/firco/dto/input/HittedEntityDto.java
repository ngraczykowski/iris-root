package com.silenteight.payments.bridge.firco.dto.input;

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
  private String id; // "GSN0002899"

  @JsonProperty("IsException")
  private String isException; // "0"

  @JsonProperty("OfficialReferences")
  private List<RequestOfficialReferenceDto> officialReferences;

  @JsonProperty("Names")
  private List<NameDto> names;

  @JsonProperty("Addresses")
  private List<RequestAddressDto> addresses;

  @JsonProperty("Origin")
  private String origin; // "SANCTIONSGROUP"

  @JsonProperty("Designation")
  private String designation; // "XX-LISTPARTYNEX-OTH"

  @JsonProperty("Keywords")
  private List<KeywordDto> keywords;

  @JsonProperty("Type")
  private String type; // "C"

  @JsonProperty("Codes")
  private List<HittedEntityCodeDto> codes;

  @JsonProperty("AdditionalInfo")
  private String additionalInfo;
  // "This indicates the transaction is connected to a party or country sanctioned by the
  // United States. If that connection is not apparent from the transaction message, obtain from
  // our client the name of the sanctioned party or country involved, and the nature of that
  // party's or country's connection to the transaction. Then process or decline according to Group
  // sanctions procedures. Any transaction related to Iran must not be processed but must be
  // escalated to Group CMO or Group Trade as appropriate for onward referral to Group.",

  @JsonProperty("DatesOfBirth")
  private List<DateOfBirthDto> datesOfBirth;

  @JsonProperty("PlacesOfBirth")
  private List<PlaceOfBirthDto> placesOfBirth;

  @JsonProperty("Hyperlinks")
  private List<HyperlinkDto> hyperLinks;

  @JsonProperty("UserData1")
  private String userData1; // "RFM-TS.NS.OB-DENY UPDATE"

  @JsonProperty("UserData2")
  private String userData2; // ""

  @JsonProperty("Nationality")
  private String nationality; // ""

  @JsonProperty("IsPEP")
  private String isPep; // "0"

  @JsonProperty("IsFEP")
  private String isFep; // "0"

  @JsonProperty("HideOnlyIDs")
  private List<HideOnlyIdDto> hideOnlyIds;

  @JsonProperty("HideIDs")
  private List<HideIdDto> hideIds;

  @JsonProperty("HideOrigins")
  private List<HideOriginDto> hideOrigins;

  @JsonProperty("HideInTags")
  private List<HideInTagDto> hideInTags;

  @JsonProperty("HideUnits")
  private List<HideUnitDto> hideUnits;

  @JsonProperty("HideSenderReceivers")
  private List<HideSenderReceiverDto> hideSenderReceivers;
}
