package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrivateListEntity implements
    com.silenteight.hsbc.datasource.datamodel.PrivateListEntity {

  private String recordId;
  private String inputStream;
  private String listKey;
  private String listSubKey;
  private String listRecordType;
  private String listRecordOrigin;
  private String listRecordId;
  private String listRecordSubId;
  private String registrationNumber;
  private String entityNameOriginal;
  private String entityNameDerived;
  private String nameType;
  private String nameQuality;
  private String primaryName;
  private String originalScriptName;
  private String aliasIsAcronym;
  private String vesselIndicator;
  private String vesselInfo;
  private String address;
  private String city;
  private String state;
  private String postalCode;
  private String addressCountry;
  private String registrationCountry;
  private String operatingCountries;
  private String countryCodesAll;
  private String countriesAll;
  private String profileHyperlink;
  private String searchHyperlink;
  private String linkedProfiles;
  private String linkedRelationships;
  private BigDecimal riskScore;
  private BigDecimal pepRiskScore;
  private BigDecimal dataConfidenceScore;
  private String dataConfidenceComment;
  private String inactiveFlag;
  private String inactiveSinceDate;
  private String pepClassification;
  private String addedDate;
  private String lastUpdatedDate;
  private String addressType;
  private String currentAddress;
  private String telephoneNumber;
  private String edqBusinessTelephoneNumber;
  private String telephoneNumberBusiness;
  private String edqCellNumberPersonal;
  private String edqTaxNumber;
  private String edqEmailAddress;
  private String scionReferenceNumber;
  private String role;
  private String listCategory;
  private String furtherInfo;
  private String edqBusinessType;
  private String dateOfIncorporation;
  private String edqDataOfIncorporationString;
  private String externalCustomerId;
  private String listCustomerType;
  private String edqListName;
  private String edqWatchlistLstType;
  private String primaryName1;
  private String entityName;
  private String entityName1;
  private String entityName2;
}
