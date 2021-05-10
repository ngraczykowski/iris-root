package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class CustomerEntity {

  @JsonProperty("CustomerEntities.Record Id")
  private String recordId;
  @JsonProperty("CustomerEntities.Input Stream")
  private String inputStream;
  @JsonProperty("CustomerEntities.Source System History ID")
  private Integer sourceSystemHistoryId;
  @JsonProperty("CustomerEntities.Close Of Business Date")
  private String closeOfBusinessDate;
  @JsonProperty("CustomerEntities.Partition Number")
  private Integer partitionNumber;
  @JsonProperty("CustomerEntities.Source System Identifier")
  private String sourceSystemIdentifier;
  @JsonProperty("CustomerEntities.External Profile ID")
  private String externalProfileId;
  @JsonProperty("CustomerEntities.Concatenated Profile ID")
  private String concatenatedProfileId;
  @JsonProperty("CustomerEntities.Profile Type")
  private String profileType;
  @JsonProperty("CustomerEntities.Party Role Type Code")
  private String partyRoleTypeCode;
  @JsonProperty("CustomerEntities.Profile Status")
  private String profileStatus;
  @JsonProperty("CustomerEntities.Profile Segment")
  private String profileSegment;
  @JsonProperty("CustomerEntities.LoB Name")
  private String lobName;
  @JsonProperty("CustomerEntities.LoB Region")
  private String lobRegion;
  @JsonProperty("CustomerEntities.LoB Country")
  private String lobCountry;
  @JsonProperty("CustomerEntities.HSBC Legal Entity Code")
  private String hsbcLegalEntityCode;
  @JsonProperty("CustomerEntities.Name Category Code")
  private String nameCategoryCode;
  @JsonProperty("CustomerEntities.NameTypeCode")
  private String nameTypeCode;
  @JsonProperty("CustomerEntities.Name Language Type Code")
  private String nameLanguageTypeCode;
  @JsonProperty("CustomerEntities.Entity Name (Original)")
  private String entityNameOriginal;
  @JsonProperty("CustomerEntities.PEP Indicator")
  private String pepIndicator;
  @JsonProperty("CustomerEntities.Special Category Customer")
  private String specialCategoryCustomer;
  @JsonProperty("CustomerEntities.Source Address Type")
  private String sourceAddressType;
  @JsonProperty("CustomerEntities.AddressFormatCode")
  private String addressFormatCode;
  @JsonProperty("CustomerEntities.Address Language Type Code")
  private String addressLanguageTypeCode;
  @JsonProperty("CustomerEntities.Source Address Line 1")
  private String sourceAddressLine1;
  @JsonProperty("CustomerEntities.Source Address Line 2")
  private String sourceAddressLine2;
  @JsonProperty("CustomerEntities.Source Address Line 3")
  private String sourceAddressLine3;
  @JsonProperty("CustomerEntities.Source Address Line 4")
  private String sourceAddressLine4;
  @JsonProperty("CustomerEntities.Source Address Line 5")
  private String sourceAddressLine5;
  @JsonProperty("CustomerEntities.Source Address Line 6")
  private String sourceAddressLine6;
  @JsonProperty("CustomerEntities.Source Address Line 7")
  private String sourceAddressLine7;
  @JsonProperty("CustomerEntities.Source Address Line 8")
  private String sourceAddressLine8;
  @JsonProperty("CustomerEntities.Source Address Line 9")
  private String sourceAddressLine9;
  @JsonProperty("CustomerEntities.Source Address Line 10")
  private String sourceAddressLine10;
  @JsonProperty("CustomerEntities.Source Postal Code")
  private String sourcePostalCode;
  @JsonProperty("CustomerEntities.Source Address Country")
  private String sourceAddressCountry;
  @JsonProperty("CustomerEntities.Identification Document 1")
  private String identificationDocument1;
  @JsonProperty("CustomerEntities.Identification Document 2")
  private String identificationDocument2;
  @JsonProperty("CustomerEntities.Identification Document 3")
  private String identificationDocument3;
  @JsonProperty("CustomerEntities.Identification Document 4")
  private String identificationDocument4;
  @JsonProperty("CustomerEntities.Identification Document 5")
  private String identificationDocument5;
  @JsonProperty("CustomerEntities.Identification Document 6")
  private String identificationDocument6;
  @JsonProperty("CustomerEntities.Identification Document 7")
  private String identificationDocument7;
  @JsonProperty("CustomerEntities.Identification Document 8")
  private String identificationDocument8;
  @JsonProperty("CustomerEntities.Identification Document 9")
  private String identificationDocument9;
  @JsonProperty("CustomerEntities.Identification Document 10")
  private String identificationDocument10;
  @JsonProperty("CustomerEntities.Trades With Countries")
  private String tradesWithCountries;
  @JsonProperty("CustomerEntities.Subsidiaries Operates in Countries")
  private String subsidiariesOperatesInCountries;
  @JsonProperty("CustomerEntities.Export Countries")
  private String exportCountries;
  @JsonProperty("CustomerEntities.Import Countries")
  private String importCountries;
  @JsonProperty("CustomerEntities.Countries of Incorporation")
  private String countriesOfIncorporation;
  @JsonProperty("CustomerEntities.Countries of Registration (Original)")
  private String countriesOfRegistrationOriginal;
  @JsonProperty("CustomerEntities.Countries of Business")
  private String countriesOfBusiness;
  @JsonProperty("CustomerEntities.Countries of Head Office")
  private String countriesOfHeadOffice;
  @JsonProperty("CustomerEntities.edqCustID")
  private String edqCustId;
  @JsonProperty("CustomerEntities.edqCustSubID")
  private String edqCustSubId;
  @JsonProperty("CustomerEntities.edqLoBCountryCode")
  private String edqLobCountryCode;
  @JsonProperty("CustomerEntities.Address Country")
  private String addressCountry;
  @JsonProperty("CustomerEntities.edqAddressCountryCode")
  private String edqAddressCountryCode;
  @JsonProperty("CustomerEntities.edqTradesWithCountries")
  private String edqTradesWithCountries;
  @JsonProperty("CustomerEntities.edqExportsCountries")
  private String edqExportsCountries;
  @JsonProperty("CustomerEntities.edqImportsCountries")
  private String edqImportsCountries;
  @JsonProperty("CustomerEntities.edqIncorporationCountries")
  private String edqIncorporationCountries;
  @JsonProperty("CustomerEntities.edqIncorporationCountriesCodes")
  private String edqIncorporationCountriesCodes;
  @JsonProperty("CustomerEntities.Registration Country")
  private String registrationCountry;
  @JsonProperty("CustomerEntities.edqRegiistrationCountriesCodes")
  private String edqRegistrationCountriesCodes;
  @JsonProperty("CustomerEntities.edqBusinessCountries")
  private String edqBusinessCountries;
  @JsonProperty("CustomerEntities.edqHeadOfficeCountries")
  private String edqHeadOfficeCountries;
  @JsonProperty("CustomerEntities.Countries (All)")
  private String countriesAll;
  @JsonProperty("CustomerEntities.Countries (All) Codes")
  private String countriesAllCodes;
  @JsonProperty("CustomerEntities.edqLoB")
  private String edqLob;
  @JsonProperty("CustomerEntities.edqPermission")
  private String edqPermission;
  @JsonProperty("CustomerEntities.Tax ID")
  private String taxId;
  @JsonProperty("CustomerEntities.City")
  private String city;
  @JsonProperty("CustomerEntities.Profile Hyperlink")
  private String profileHyperlink;
  @JsonProperty("CustomerEntities.Search Hyperlink")
  private String searchHyperlink;
  @JsonProperty("CustomerEntities.edqListKey")
  private String edqListKey;
  @JsonProperty("CustomerEntities.Address")
  private String address;
  @JsonProperty("CustomerEntities.Entity Name")
  private String entityName;
  @JsonProperty("CustomerEntities.Postal Code")
  private String postalCode;
  @JsonProperty("CustomerEntities.Person or Business Indicator")
  private String personOrBusinessIndicator;
  @JsonProperty("CustomerEntities.edqCloseOfBusinessDate")
  private String edqCloseOfBusinessDate;
  @JsonProperty("CustomerEntities.Operating Countries")
  private String operatingCountries;
  @JsonProperty("CustomerEntities.edqOperatingCountriesCodes")
  private String edqOperatingCountriesCodes;
  @JsonProperty("CustomerEntities.Registration Number")
  private String registrationNumber;
  @JsonProperty("CustomerEntities.Profile Name Type")
  private String profileNameType;
  @JsonProperty("CustomerEntities.edqPartyRoleTypeDescription")
  private String edqPartyRoleTypeDescription;
  @JsonProperty("CustomerEntities.edqPartyStatusCodeDescription")
  private String edqPartyStatusCodeDescription;
  @JsonProperty("CustomerEntities.edqDay1SpikeFlag")
  private String edqDay1SpikeFlag;
  @JsonProperty("CustomerEntities.Original Script Name")
  private String originalScriptName;
  @JsonProperty("CustomerEntities.Profile Full Address")
  private String profileFullAddress;
  @JsonProperty("CustomerEntities.edqScreeningMode")
  private String edqScreeningMode;
  @JsonProperty("CustomerEntities.edqCaseKey")
  private String edqCaseKey;
  @JsonProperty("CustomerEntities.Address Type")
  private String addressType;
  @JsonProperty("CustomerEntities.SSC Codes")
  private String sscCodes;
  @JsonProperty("CustomerEntities.CTRP Fragment")
  private String ctrpFragment;
  @JsonProperty("CustomerEntities.Request User Name")
  private String requestUserName;
  @JsonProperty("CustomerEntities.Request Date/Time")
  private String requestDateTime;
}
