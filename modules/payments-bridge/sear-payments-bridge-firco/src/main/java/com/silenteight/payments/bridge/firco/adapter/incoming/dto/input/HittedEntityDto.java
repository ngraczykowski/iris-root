package com.silenteight.payments.bridge.firco.adapter.incoming.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class HittedEntityDto implements Serializable {

  private static final long serialVersionUID = 7668869052958833725L;

  @JsonProperty("ID")
  private String id; // "GSN0002899"

  private String isException; // "0"

  private List<RequestOfficialReferenceDto> officialReferences;

  private List<NameDto> names;

  private List<RequestAddressDto> addresses;

  private String origin; // "SANCTIONSGROUP"

  private String designation; // "XX-LISTPARTYNEX-OTH"

  private List<KeywordDto> keywords;

  private String type; // "C"

  private List<HittedEntityCodeDto> codes;

  private String additionalInfo;
  // "This indicates the transaction is connected to a party or country sanctioned by the
  // United States. If that connection is not apparent from the transaction message, obtain from
  // our client the name of the sanctioned party or country involved, and the nature of that
  // party's or country's connection to the transaction. Then process or decline according to Group
  // sanctions procedures. Any transaction related to Iran must not be processed but must be
  // escalated to Group CMO or Group Trade as appropriate for onward referral to Group.",

  private List<DateOfBirthDto> datesOfBirth;

  private List<PlaceOfBirthDto> placesOfBirth;

  private List<HyperlinkDto> hyperlinks;

  private String userData1; // "RFM-TS.NS.OB-DENY UPDATE"

  private String userData2; // ""

  private String nationality; // ""

  private String isPep; // "0"

  private String isFep; // "0"

  @JsonProperty("HideOnlyIDs")
  private List<HideOnlyIdDto> hideOnlyIds;

  @JsonProperty("HideIDs")
  private List<HideIdDto> hideIds;

  private List<HideOriginDto> hideOrigins;

  private List<HideInTagDto> hideInTags;

  private List<HideUnitDto> hideUnits;

  private List<HideSenderReceiverDto> hideSenderReceivers;
}
