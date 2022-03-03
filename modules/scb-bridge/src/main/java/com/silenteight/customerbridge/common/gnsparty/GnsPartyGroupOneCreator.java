package com.silenteight.customerbridge.common.gnsparty;

import com.silenteight.commons.collections.MapBuilder;

import java.util.List;
import java.util.Map;

import static com.silenteight.customerbridge.common.gnsparty.GnsPartyCommonFields.NAME;
import static com.silenteight.customerbridge.common.gnsparty.GnsPartyCommonFields.NATIONAL_IDS;
import static com.silenteight.customerbridge.common.gnsparty.GnsPartyCommonFields.PASSPORT_NUMBERS;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

class GnsPartyGroupOneCreator extends AbstractGnsPartyCreator {

  private static final String NATIONAL_IDENTITY_NUMBER_FIELD = "nationalIdentityNumber";
  private static final String PASSPORT_NUMBER_FIELD = "passportNumber";

  private static final Map<String, String> FIELD_MAP = MapBuilder.from(
      "accId", "customerIdentificationNo",
      "customerType", "clientType",
      "streetAddress", "registOrResidentialAddress",
      "countryOfRegistration", "registOrResidenAddCntry",
      "countryOfIncorporation", "nationalityAll",
      "countryOfOrigin", "countryOfBirthOrRegist",
      "dateOfBirth", "dateOfBirthOrRegis",
      "amlCountry", "bookingLocation");

  private static final List<String> FIELDS = asList(
      "sourceSystemIdentifier",
      "accId",
      "customerType",
      "addressSequenceNumber",
      "title",
      "accountName",
      "gender",
      "streetAddress",
      "city",
      "state",
      "countryOfRegistration",
      "countryOfIncorporation",
      "countryOfOrigin",
      NATIONAL_IDENTITY_NUMBER_FIELD,
      PASSPORT_NUMBER_FIELD,
      "dateOfBirth",
      "placeOfBirth",
      "phone",
      "fax",
      "email",
      "relationshipManager",
      "bic",
      "amlCountry",
      "customerStatus",
      "subClassification",
      "supplementaryInformation1",
      "supplementaryInformation2",
      "supplementaryInformation3",
      "supplementaryInformation4",
      "supplementaryInformation5",
      "supplementaryInformation6",
      "supplementaryInformation7",
      "supplementaryInformation8",
      "supplementaryInformation9",
      "supplementaryInformation10");

  GnsPartyGroupOneCreator() {
    super(FIELDS, FIELD_MAP);
  }

  @Override
  protected GnsParty createAdditionalFields(GnsParty result) {
    result.copyField("accountName", NAME);

    result.createList(
        "supplementaryInformations",
        asList(
            "supplementaryInformation1",
            "supplementaryInformation2",
            "supplementaryInformation3",
            "supplementaryInformation4",
            "supplementaryInformation5",
            "supplementaryInformation6",
            "supplementaryInformation7",
            "supplementaryInformation8",
            "supplementaryInformation9",
            "supplementaryInformation10"));

    result.createNamedMap(
        "identificationsMap",
        MapBuilder.from(PASSPORT_NUMBER_FIELD, PASSPORT_NUMBER_FIELD,
            NATIONAL_IDENTITY_NUMBER_FIELD, NATIONAL_IDENTITY_NUMBER_FIELD,
            "BIC", "BIC"));

    result.createList(PASSPORT_NUMBERS, singletonList(PASSPORT_NUMBERS));

    result.createList(NATIONAL_IDS, singletonList(NATIONAL_IDENTITY_NUMBER_FIELD));

    result.createList(
        "identifications",
        asList(
            PASSPORT_NUMBER_FIELD,
            NATIONAL_IDENTITY_NUMBER_FIELD,
            "BIC"));

    result.createList(
        "nationalities",
        asList(
            "countryOfRegistration",
            "countryOfIncorporation",
            "countryOfOrigin"));

    result.createList(
        "residencies",
        asList(
            "countryOfRegistration",
            "countryOfIncorporation",
            "countryOfOrigin"));

    result.createList(
        "residentialAddresses",
        asList(
            "registOrResidentialAddress",
            "city",
            "state"));

    return result;
  }
}