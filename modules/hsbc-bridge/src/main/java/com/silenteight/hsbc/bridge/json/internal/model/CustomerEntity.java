package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

@Data
public class CustomerEntity implements com.silenteight.hsbc.datasource.datamodel.CustomerEntity {

  private String recordId;
  private String inputStream;
  private String sourceSystemHistoryId;
  private String closeOfBusinessDate;
  private String partitionNumber;
  private String sourceSystemIdentifier;
  private String externalProfileId;
  private String concatenatedProfileId;
  private String profileType;
  private String partyRoleTypeCode;
  private String profileStatus;
  private String profileSegment;
  private String lobName;
  private String lobRegion;
  private String lobCountry;
  private String hsbcLegalEntityCode;
  private String nameCategoryCode;
  private String nameTypeCode;
  private String nameLanguageTypeCode;
  private String entityNameOriginal;
  private String pepIndicator;
  private String specialCategoryCustomer;
  private String sourceAddressType;
  private String addressFormatCode;
  private String addressLanguageTypeCode;
  private String sourceAddressLine1;
  private String sourceAddressLine2;
  private String sourceAddressLine3;
  private String sourceAddressLine4;
  private String sourceAddressLine5;
  private String sourceAddressLine6;
  private String sourceAddressLine7;
  private String sourceAddressLine8;
  private String sourceAddressLine9;
  private String sourceAddressLine10;
  private String sourcePostalCode;
  private String sourceAddressCountry;
  private String identificationDocument1;
  private String identificationDocument2;
  private String identificationDocument3;
  private String identificationDocument4;
  private String identificationDocument5;
  private String identificationDocument6;
  private String identificationDocument7;
  private String identificationDocument8;
  private String identificationDocument9;
  private String identificationDocument10;
  private String tradesWithCountries;
  private String subsidiariesOperatesInCountries;
  private String exportCountries;
  private String importCountries;
  private String countriesOfIncorporation;
  private String countriesOfRegistrationOriginal;
  private String countriesOfBusiness;
  private String countriesOfHeadOffice;
  private String edqCustId;
  private String edqCustSubId;
  private String edqLobCountryCode;
  private String addressCountry;
  private String edqAddressCountryCode;
  private String edqTradesWithCountries;
  private String edqExportsCountries;
  private String edqImportsCountries;
  private String edqIncorporationCountries;
  private String edqIncorporationCountriesCodes;
  private String registrationCountry;
  private String edqRegistrationCountriesCodes;
  private String edqBusinessCountries;
  private String edqHeadOfficeCountries;
  private String countriesAll;
  private String countriesAllCodes;
  private String edqLob;
  private String edqPermission;
  private String taxId;
  private String city;
  private String profileHyperlink;
  private String searchHyperlink;
  private String edqListKey;
  private String address;
  private String entityName;
  private String postalCode;
  private String personOrBusinessIndicator;
  private String edqCloseOfBusinessDate;
  private String operatingCountries;
  private String edqOperatingCountriesCodes;
  private String registrationNumber;
  private String profileNameType;
  private String edqPartyRoleTypeDescription;
  private String edqPartyStatusCodeDescription;
  private String edqDay1SpikeFlag;
  private String originalScriptName;
  private String profileFullAddress;
  private String edqScreeningMode;
  private String edqCaseKey;
  private String addressType;
  private String sscCodes;
  private String ctrpFragment;
  private String requestUserName;
  private String requestDateTime;
}
