package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrivateListIndividual implements
    com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual {

  private String recordId;
  private String inputStream;
  private String listKey;
  private String listSubKey;
  private String listRecordType;
  private String listRecordOrigin;
  private String listRecordId;
  private String listRecordSubId;
  private String passportNumber;
  private String nationalId;
  private String title;
  private String fullNameOriginal;
  private String givenNamesOriginal;
  private String familyNameOriginal;
  private String fullNameDerived;
  private String givenNamesDerived;
  private String familyNameDerived;
  private String nameType;
  private String nameQuality;
  private String primaryName;
  private String originalScriptName;
  private String gender;
  private String genderDerivedFlag;
  private String dateOfBirth;
  private Integer yearOfBirth;
  private String deceasedFlag;
  private String deceasedDate;
  private String occupation;
  private String address;
  private String city;
  private String placeOfBirth;
  private String postalCode;
  private String addressCountry;
  private String residencyCountry;
  private String countryOfBirth;
  private String nationalities;
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
  private String pEPClassification;
  private String addedDate;
  private String lastUpdatedDate;
  private String edqAddressType;
  private String edqCurrentAddress;
  private String edqTelephoneNumber;
  private String edqCellNumberBusiness;
  private String edqCellNumberPersonal;
  private String edqTaxNumber;
  private String edqDrivingLicence;
  private String edqEmailAddress;
  private String edqNiNumber;
  private String edqScionRef;
  private String edqListCustomerType;
  private String edqSuffix;
  private String edqRole;
  private String edqListCategory;
  private String edqFurtherInfo;
  private String edqSsn;
  private String edqCustomerId;
  private String edqListName;
  private String edqWatchListType;
  private String middleName1Derived;
  private String middleName2Derived;
  private String middleName1Original;
  private String middleName2Original;
  private String currentAddress;
  private String recordType;
}
