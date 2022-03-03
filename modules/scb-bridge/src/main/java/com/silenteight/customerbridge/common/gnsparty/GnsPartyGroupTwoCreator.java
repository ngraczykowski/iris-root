package com.silenteight.customerbridge.common.gnsparty;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.customerbridge.common.gnsparty.GnsPartyCommonFields.ALTERNATE_NAMES;
import static com.silenteight.customerbridge.common.gnsparty.GnsPartyCommonFields.NAME;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;

class GnsPartyGroupTwoCreator extends AbstractGnsPartyCreator {

  private static final List<String> FIELDS = asList(
      "sourceSystemIdentifier",
      "customerIdentificationNo",
      "clientType",
      "fullLegalName",
      "fullTradingName",
      "legalParentGroupName",
      "executiveManagementNames",
      "supplimentaryCardName",
      "alternateName1",
      "alternateName2",
      "alternateName3",
      "alternateNameRest",
      "registOrResidentialAddress",
      "registOrResidenAddCntry",
      "mailingOrCommunicationAddr",
      "mailingOrCommunAddrCntry",
      "operatingOrOfficialAddress",
      "operatingOrOffAddrCntry",
      "otherAddress",
      "otherAddressCountry",
      "registeredAddOfHeadOffice",
      "registeredAddCntryOfHo",
      "registeredAddOfParentComp",
      "regAddCntryParCompany",
      "nationalityAll",
      "estCountryOfHeadOffice",
      "estCountryOfParentCompany",
      "identificationType1",
      "identificationNumber1",
      "identificationType2",
      "identificationNumber2",
      "identificationType3",
      "identificationNumber3",
      "identificationType4",
      "identificationNumber4",
      "identificationTypeRest",
      "identificationNumberRest",
      "dateOfBirthOrRegis",
      "countryOfBirthOrRegist",
      "homauthGovtCntryName",
      "nameOfStockExchange",
      "nameOfAuthority",
      "bookingLocation",
      "businessNature",
      "cddRiskRating",
      "classOfBeneficiary",
      "clientLegalEntityType",
      "clientSegment",
      "clientSubSegment",
      "customerStatus",
      "accountRelaProfClsdDate",
      "lastCddApprovedDate",
      "linkedCustomerIdentNo",
      "natureRelaHomeStateAuth",
      "ownershipStatusOfClient",
      "partyType",
      "pepStatus",
      "relatedPartyType",
      "relationshPrimaryCardholder",
      "rmCodeCddOwner",
      "rmLocationCddOwner",
      "staffAccountIdentifier",
      "gender",
      "countryOfEmployment",
      "departmentName",
      "departmentId",
      "casaFlag",
      "bankCd",
      "changeIndicator",
      "dateTimeStamp");

  GnsPartyGroupTwoCreator() {
    super(FIELDS, emptyMap());
  }

  GnsPartyGroupTwoCreator(List<String> additionalFields) {
    super(mergeAdditionalFields(additionalFields), new HashMap<>());
  }

  private static List<String> mergeAdditionalFields(List<String> additionalFields) {
    return Stream.concat(FIELDS.stream(), additionalFields.stream()).collect(toList());
  }

  @Override
  protected GnsParty createAdditionalFields(GnsParty result) {
    result.copyField("fullLegalName", NAME);

    result.createList(ALTERNATE_NAMES, getAlternateNames());

    var identifications = GnsPartyIdentifications.fromGnsParty(result);
    result.addPassportNumbers(identifications.collectPassportNumbers());
    result.addNationalIds(identifications.collectNationalIds());

    result.createList(
        "identifications",
        getIdentificationList());

    result.createList(
        "nationalities",
        asList(
            "nationalityAll",
            "countryOfBirthOrRegist"));

    result.createList(
        "residencies",
        asList(
            "registOrResidenAddCntry",
            "mailingOrCommunAddrCntry",
            "operatingOrOffAddrCntry",
            "otherAddressCountry",
            "registeredAddCntryOfHo",
            "regAddCntryParCompany",
            "estCountryOfHeadOffice",
            "estCountryOfParentCompany"));

    result.createList(
        "residentialAddresses",
        asList(
            "registOrResidentialAddress",
            "mailingOrCommunicationAddr",
            "operatingOrOfficialAddress",
            "otherAddress",
            "registeredAddOfHeadOffice",
            "registeredAddOfParentComp",
            "nameOfAuthority"));

    return result;
  }

  protected static List<String> getAlternateNames() {
    return asList(
        "fullLegalName",
        "fullTradingName",
        "legalParentGroupName",
        "executiveManagementNames",
        "supplimentaryCardName",
        "alternateName1",
        "alternateName2",
        "alternateName3",
        "alternateNameRest");
  }

  protected List<String> getIdentificationList() {
    return asList(
        "identificationNumber1",
        "identificationNumber2",
        "identificationNumber3",
        "identificationNumber4",
        "identificationNumberRest");
  }
}
