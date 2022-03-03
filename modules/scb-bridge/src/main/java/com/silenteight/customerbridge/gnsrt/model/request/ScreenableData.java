package com.silenteight.customerbridge.gnsrt.model.request;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
public class ScreenableData {

  @JsonProperty("amlcountry")
  @NotNull
  private String amlCountry = null;

  @JsonProperty("sourceSystemIdentifier")
  @NotNull
  private String sourceSystemIdentifier = null;

  @JsonProperty("customerIdentificationNo")
  private String customerIdentificationNo = null;

  @JsonProperty("clientType")
  private String clientType = null;

  @JsonProperty("fullLegalName")
  private String fullLegalName = null;

  @JsonProperty("fullTradingName")
  private String fullTradingName = null;

  @JsonProperty("legalParentOrGroupName")
  private String legalParentOrGroupName = null;

  @JsonProperty("executiveManagementNames")
  private String executiveManagementNames = null;

  @JsonProperty("supplementaryCardName")
  private String supplementaryCardName = null;

  @JsonProperty("alternateName1")
  private String alternateName1 = null;

  @JsonProperty("alternateName2")
  private String alternateName2 = null;

  @JsonProperty("alternateName3")
  private String alternateName3 = null;

  @JsonProperty("alternateNameRest")
  private String alternateNameRest = null;

  @JsonProperty("registeredOrResidentialAddress")
  private String registeredOrResidentialAddress = null;

  @JsonProperty("registeredOrResidentialAddressCountry")
  private String registeredOrResidentialAddressCountry = null;

  @JsonProperty("mailingOrCommunicationAddress")
  private String mailingOrCommunicationAddress = null;

  @JsonProperty("mailingOrCommunicationAddressCountry")
  private String mailingOrCommunicationAddressCountry = null;

  @JsonProperty("operatingOrOfficialAddress")
  private String operatingOrOfficialAddress = null;

  @JsonProperty("operatingOrOfficialAddressCountry")
  private String operatingOrOfficialAddressCountry = null;

  @JsonProperty("otherAddress")
  private String otherAddress = null;

  @JsonProperty("otherAddressCountry")
  private String otherAddressCountry = null;

  @JsonProperty("registeredAddressOfHeadOffice")
  private String registeredAddressOfHeadOffice = null;

  @JsonProperty("registeredAddressCountryOfHeadOffice")
  private String registeredAddressCountryOfHeadOffice = null;

  @JsonProperty("registeredAddressOfParentCompany")
  private String registeredAddressOfParentCompany = null;

  @JsonProperty("registeredAddressCountryOfParentCompany")
  private String registeredAddressCountryOfParentCompany = null;

  @JsonProperty("nationalityAll")
  private String nationalityAll = null;

  @JsonProperty("establishmentCountryOfHO")
  private String establishmentCountryOfHO = null;

  @JsonProperty("establishmentCountryOfParentCompany")
  private String establishmentCountryOfParentCompany = null;

  @JsonProperty("identificationType1")
  private String identificationType1 = null;

  @JsonProperty("identificationNumber1")
  private String identificationNumber1 = null;

  @JsonProperty("identificationType2")
  private String identificationType2 = null;

  @JsonProperty("identificationNumber2")
  private String identificationNumber2 = null;

  @JsonProperty("identificationType3")
  private String identificationType3 = null;

  @JsonProperty("identificationNumber3")
  private String identificationNumber3 = null;

  @JsonProperty("identificationType4")
  private String identificationType4 = null;

  @JsonProperty("identificationNumber4")
  private String identificationNumber4 = null;

  @JsonProperty("identificationTypeRest")
  private String identificationTypeRest = null;

  @JsonProperty("identificationNumberRest")
  private String identificationNumberRest = null;

  @JsonProperty("dateOfBirthOrRegistration")
  private String dateOfBirthOrRegistration = null;

  @JsonProperty("countryOfBirthOrRegistration")
  private String countryOfBirthOrRegistration = null;

  @JsonProperty("homeStateAuthorityOrGovtCountryName")
  private String homeStateAuthorityOrGovtCountryName = null;

  @JsonProperty("nameOfStockExchange")
  private String nameOfStockExchange = null;

  @JsonProperty("nameOfAuthority")
  private String nameOfAuthority = null;

  @JsonProperty("businessNature")
  private String businessNature = null;

  @JsonProperty("clientLegalEntityType")
  private String clientLegalEntityType = null;

  @JsonProperty("gender")
  private String gender = null;

  @JsonProperty("countryOfEmployment")
  private String countryOfEmployment = null;

  @JsonProperty("addressCity")
  private String addressCity = null;

  @JsonProperty("dateTimeStamp")
  private String dateTimeStamp = null;

  @JsonProperty("supplementaryInformation1")
  private String supplementaryInformation1 = null;

  @JsonProperty("supplementaryInformation2")
  private String supplementaryInformation2 = null;

  @JsonProperty("supplementaryInformation3")
  private String supplementaryInformation3 = null;

  @JsonProperty("supplementaryInformation4")
  private String supplementaryInformation4 = null;

  @JsonProperty("supplementaryInformation5")
  private String supplementaryInformation5 = null;

  @JsonProperty("supplementaryInformation6")
  private String supplementaryInformation6 = null;

  @JsonProperty("supplementaryInformation7")
  private String supplementaryInformation7 = null;

  @JsonProperty("supplementaryInformation8")
  private String supplementaryInformation8 = null;

  @JsonProperty("supplementaryInformation9")
  private String supplementaryInformation9 = null;

  @JsonProperty("supplementaryInformation10")
  private String supplementaryInformation10 = null;

  @JsonProperty("partyType")
  private String partyType = null;

  @JsonProperty("supplierType")
  private String supplierType = null;

  @JsonProperty("acctOrRelationshipOrProfileClosedDate")
  private String acctOrRelationshipOrProfileClosedDate = null;

  @JsonProperty("lastCDDApprovedDate")
  private String lastCddApprovedDate = null;

  @JsonProperty("linkedCustomerIdentificationNo")
  private String linkedCustomerIdentificationNo = null;

  @JsonProperty("natureOfRelationshipWithHomeStateAuthority")
  private String natureOfRelationshipWithHomeStateAuthority = null;

  @JsonProperty("ownershipStatusOfClient")
  private String ownershipStatusOfClient = null;

  @JsonProperty("relationshipToThePrimaryCardholder")
  private String relationshipToThePrimaryCardholder = null;

  @JsonProperty("clientSegment")
  private String clientSegment = null;

  @JsonProperty("clientSubSegment")
  private String clientSubSegment = null;

  @JsonProperty("classesOfBeneficiary")
  private String classesOfBeneficiary = null;

  @JsonProperty("customerStatus")
  private String customerStatus = null;

  @JsonProperty("staffAccountIdentifier")
  private String staffAccountIdentifier = null;

  @JsonProperty("relatedPartyType")
  private String relatedPartyType = null;

  @JsonProperty("casaFlag")
  private String casaFlag = null;

  @JsonProperty("priorityIndicator")
  private String priorityIndicator = null;

  @JsonProperty("relianceFlgOrSuppTypOrKoreanName")
  private String relianceFlgOrSuppTypOrKoreanName = null;

  @JsonProperty("realTimeMessageIndicator")
  private String realTimeMessageIndicator = null;

  @JsonProperty("changeOrPriorityIndicator")
  private String changeOrPriorityIndicator = null;

  @JsonProperty("alternateID1Description")
  private String alternateID1Description = null;

  @JsonProperty("alternateID1")
  private String alternateID1 = null;

  @JsonProperty("alternateID2Description")
  private String alternateID2Description = null;

  @JsonProperty("alternateID2")
  private String alternateID2 = null;

  @JsonProperty("nottobeused")
  private Object nottobeused = null;

  @JsonProperty("cddriskRating")
  private String cddriskRating = null;

  @JsonProperty("pepstatus")
  private String pepstatus = null;

  @JsonProperty("rmcodeOfCDDOwner")
  private String rmcodeOfCddOwner;

  @JsonProperty("rmlocationOfCDDOwner")
  private String rmlocationOfCddOwner;
}
