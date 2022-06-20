/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty;

import com.silenteight.commons.collections.MapBuilder;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty.GnsPartyCommonFields.ALTERNATE_NAMES;
import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty.GnsPartyCommonFields.NAME;
import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty.PartyCreatorProvider.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

public class RecordParserTest {

  private static final char CHAR_SEP = '~';
  private static final String GROUP_ONE_VALUE =
      "(CLOB) CCMS~HKCCNS600~I~1~~John Smith~~75 W Mondamin St, Minooka, IL 60447, USA"
          + "~~~~~~118296867~~19690926~~~~~~~HK~ACTIVE~HKCB~1~D693360~~~~~~~~";

  private static final String GROUP_TWO_VALUE =
      "(CLOB) ECDDP~1008000330_ABC00349762_803f3b8b-2a15-439d-8ab6-1585756ad5f4~U~"
          + "Armir TAPTA~~~~~1~2~~~APPARTMENT 5/A WEST, SAVOY COURT,150 ROBINSON ROAD,MIDLEVELS,"
          + "SINGAPOUR~~~~~~~~~~~~IN~~~type1~number1~~~~~~~~~1985-09-24~~~~~HK~~M~~Private Company~"
          + "Middle Market~Middle Markets~ACTIVE~~2015-05-13~13526370_ABC00349762_1~~~RELATED "
          + "PARTY~U~|Authorised Signatory|Beneficial Owner|Director|~~1257742~~~~~~~~~F~"
          + "2011-04-10 14:40:35.107";

  private static final String GROUP_FOUR_VALUE =
      "(CLOB) SCRTS~87654321~~~~~I~~Full Name~~USA~~~75 W Mondamin St, Minooka, IL 60447, USA"
          + "~1990-10-03~2019-05-10 08:32:32.117~~~~~~~~~~";

  private static final String GROUP_FOUR_VALUE_AGGREGATION_FIELDS =
      "(CLOB) SCRTS~1~2~3~4~5~6~7~8~9~10~11~12~13~14~15~16~17~18~19~20~21~22~23~24~25";

  private static final String GROUP_FIVE_VALUE =
      "(CLOB) ICDD~1008000330_ABC00349762_803f3b8b-2a15-439d-8ab6-1585756ad5f4~U~"
          + "Armir TAPTA~~~~~1~2~~~APPARTMENT 5/A WEST, SAVOY COURT,150 ROBINSON ROAD,MIDLEVELS,"
          + "SINGAPOUR~~~~~~~~~~~~IN~~~type1~number1~~~~~~~~~1985-09-24~~~~~HK~~M~~Private Company~"
          + "Middle Market~Middle Markets~ACTIVE~~2015-05-13~13526370_ABC00349762_1~~~RELATED "
          + "PARTY~U~|Authorised Signatory|Beneficial Owner|Director|~~1257742~~~~~~~~~F~"
          + "2011-04-10 14:40:35.107~~number2~~number3~~~~~~~~~~";

  private static final String RECORD =
      "(CLOB) EBBS~1008000330_ABC00349762_803f3b8b-2a15-439d-8ab6-1585756ad5f4~U~"
          + "Armir TAPTA~~~~~1~2~~~~Middle Markets~ACTIVE~~2015-05-13~13526370_ABC00349762_1"
          + "~~~RELATED PARTY~U~~~~1257742~~~~~~~~~F~2011-04-10 14:40:35.107~~"
          + "number2~~number3~~~~~~~~~~";

  private static final String GROUP_ONE_FMT_NAME = "SCB-ADV";
  private static final String GROUP_TWO_FMT_NAME = "SCB_EDMP_DUED";
  private static final String GROUP_FOUR_FMT_NAME = "SECURITIES_DENY";
  private static final String GROUP_FIVE_FMT_NAME = "SCB_EDMP_DUED_V1";
  private static final String SYSTEM_ID = "system_id";

  @Test
  public void countingFieldsInTestData() {
    assertThat(countFields(GROUP_ONE_VALUE, CHAR_SEP)).isEqualTo(GROUP_ONE_TOKEN_COUNT);
    assertThat(countFields(GROUP_TWO_VALUE, CHAR_SEP)).isEqualTo(GROUP_TWO_TOKEN_COUNT);
    assertThat(countFields(GROUP_FOUR_VALUE, CHAR_SEP)).isEqualTo(GROUP_FOUR_TOKEN_COUNT);
    assertThat(countFields(GROUP_FIVE_VALUE, CHAR_SEP)).isEqualTo(GROUP_FIVE_TOKEN_COUNT);
  }

  @Test
  public void wrongNumberOfColumn_emptyGnsParty() {
    GnsParty party1 = RecordParser
        .parse(SYSTEM_ID, CHAR_SEP, GROUP_ONE_FMT_NAME, "TEST~VALUE~NOT~ENOUGH~COLUMN");
    assertThat(party1).isEqualTo(GnsParty.empty());

    GnsParty party2 = RecordParser
        .parse(
            SYSTEM_ID,
            CHAR_SEP,
            GROUP_ONE_FMT_NAME,
            GROUP_TWO_VALUE + "~TEST~VALUE~TOO~MUCH~COLUMNS");
    assertThat(party2).isEqualTo(GnsParty.empty());
  }

  @Test
  public void emptyString_emptyGnsParty() {
    GnsParty party = RecordParser.parse(SYSTEM_ID, CHAR_SEP, GROUP_ONE_FMT_NAME, "");
    assertThat(party).isEqualTo(GnsParty.empty());
  }

  @Test
  public void properValueGroupOne_filledClass() {
    GnsParty result = RecordParser.parse(SYSTEM_ID, CHAR_SEP, GROUP_ONE_FMT_NAME, GROUP_ONE_VALUE);

    assertThat(result.getSourceSystemIdentifier()).isEqualTo("CCMS");
    assertThat(result.getCustomerIdentificationNo()).isEqualTo("HKCCNS600");
    assertValuesEqual(result, MapBuilder.from(
        "sourceSystemIdentifier", "CCMS",
        "customerIdentificationNo", "HKCCNS600",
        "clientType", "I",
        "accountName", "John Smith",
        "registOrResidentialAddress", "75 W Mondamin St, Minooka, IL 60447, USA",
        "dateOfBirthOrRegis", "19690926",
        "bookingLocation", "HK",
        "customerStatus", "ACTIVE",
        "identifications", singletonList("118296867"),
        "supplementaryInformations", asList("1", "D693360"),
        NAME, "John Smith"));
    assertNullValues(result, asList(
        "fullTradingName",
        "legalParentGroupName",
        "executiveManagementNames",
        "supplimentaryCardName",
        "alternateName1",
        "alternateName2",
        "alternateName3",
        "alternateNameRest",
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
        "countryOfBirthOrRegist",
        "homauthGovtCntryName",
        "nameOfStockExchange",
        "nameOfAuthority",
        "businessNature",
        "cddRiskRating",
        "classOfBeneficiary",
        "clientLegalEntityType",
        "clientSegment",
        "clientSubSegment",
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
        "dateTimeStamp"));
  }

  private static void assertValuesEqual(GnsParty gnsParty, Map<String, Object> assertions) {
    assertions.forEach((key, value) -> assertThat(gnsParty.getValue(key)).isEqualTo(value));
  }

  private static void assertNullValues(GnsParty result, List<String> nullFieldList) {
    nullFieldList.forEach(field -> assertThat(result.getValue(field)).isNull());
  }

  @Test
  public void properValueGroupTwo_filledClass() {
    GnsParty result = RecordParser.parse(SYSTEM_ID, CHAR_SEP, GROUP_TWO_FMT_NAME, GROUP_TWO_VALUE);

    assertThat(result.getSourceSystemIdentifier()).isEqualTo("ECDDP");
    assertThat(result.getCustomerIdentificationNo())
        .isEqualTo("1008000330_ABC00349762_803f3b8b-2a15-439d-8ab6-1585756ad5f4");
    assertValuesEqual(result, MapBuilder.from(
        "sourceSystemIdentifier", "ECDDP",
        "customerIdentificationNo", "1008000330_ABC00349762_803f3b8b-2a15-439d-8ab6-1585756ad5f4",
        "clientType", "U",
        "fullLegalName", "Armir TAPTA",
        "alternateName1", "1",
        "alternateName2", "2",
        "registOrResidentialAddress",
        "APPARTMENT 5/A WEST, SAVOY COURT,150 ROBINSON ROAD,MIDLEVELS,SINGAPOUR",
        "nationalityAll", "IN",
        "identificationType1", "type1",
        "identificationNumber1", "number1",
        "dateOfBirthOrRegis", "1985-09-24",
        "bookingLocation", "HK",
        "cddRiskRating", "M",
        "clientLegalEntityType", "Private Company",
        "clientSegment", "Middle Market",
        "clientSubSegment", "Middle Markets",
        "customerStatus", "ACTIVE",
        "lastCddApprovedDate", "2015-05-13",
        "linkedCustomerIdentNo", "13526370_ABC00349762_1",
        "partyType", "RELATED PARTY",
        "pepStatus", "U",
        "relatedPartyType", "|Authorised Signatory|Beneficial Owner|Director|",
        "rmCodeCddOwner", "1257742",
        "changeIndicator", "F",
        "dateTimeStamp", "2011-04-10 14:40:35.107",
        "identifications", singletonList("number1"),
        NAME, "Armir TAPTA",
        ALTERNATE_NAMES, asList("Armir TAPTA", "1", "2")));
    assertNullValues(result, asList(
        "fullTradingName",
        "legalParentGroupName",
        "executiveManagementNames",
        "supplimentaryCardName",
        "alternateName3",
        "alternateNameRest",
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
        "estCountryOfHeadOffice",
        "estCountryOfParentCompany",
        "identificationType2",
        "identificationNumber2",
        "identificationType3",
        "identificationNumber3",
        "identificationType4",
        "identificationNumber4",
        "identificationTypeRest",
        "identificationNumberRest",
        "countryOfBirthOrRegist",
        "homauthGovtCntryName",
        "nameOfStockExchange",
        "nameOfAuthority",
        "businessNature",
        "classOfBeneficiary",
        "accountRelaProfClsdDate",
        "natureRelaHomeStateAuth",
        "ownershipStatusOfClient",
        "relationshPrimaryCardholder",
        "rmLocationCddOwner",
        "staffAccountIdentifier",
        "gender",
        "countryOfEmployment",
        "departmentName",
        "departmentId",
        "casaFlag",
        "bankCd"));
  }

  @Test
  public void properValueGroupFour_filledClass() {
    GnsParty result = RecordParser
        .parse(SYSTEM_ID, CHAR_SEP, GROUP_FOUR_FMT_NAME, GROUP_FOUR_VALUE);

    assertThat(result.getSourceSystemIdentifier()).isEqualTo("SCRTS");
    assertThat(result.getCustomerIdentificationNo()).isEqualTo("87654321");
    assertValuesEqual(result, MapBuilder.from(
        "sourceSystemIdentifier", "SCRTS",
        "customerIdentificationNo", "87654321",
        "clientType", "I",
        "fullInstrumentName", "Full Name",
        "registOrResidentialAddress", "75 W Mondamin St, Minooka, IL 60447, USA",
        "registOrResidenAddCntry", "USA",
        "dateOfBirthOrRegis", "1990-10-03",
        "dateTimeStamp", "2019-05-10 08:32:32.117"));
    assertNullValues(result, asList(
        "cinsCusipCode",
        "sedolCode",
        "applicationName",
        "uniqueId",
        "assetSubtype",
        "fullNameOfIssuer",
        "issuerCountryOfIncorp",
        "issuerCountryOfOperations",
        "dateOfIncorporation",
        "issuerShareholders",
        "shareholderCountryOfIncorp",
        "shareholderCountryOfOper",
        "directorsExecutives",
        "depositoryBank",
        "fundManagersOrDistributors",
        "additionalInformation1",
        "additionalInformation2",
        "additionalInformation3",
        "gnsUploadDate",
        NAME));
  }

  @Test
  public void properValueGroupFour_aggregatedFields() {
    GnsParty result = RecordParser
        .parse(SYSTEM_ID, CHAR_SEP, GROUP_FOUR_FMT_NAME, GROUP_FOUR_VALUE_AGGREGATION_FIELDS);

    assertThat(result.getCollectionAsList("nationalities"))
        .containsExactlyInAnyOrder("10", "11", "12", "13", "17", "18");
    assertThat(result.getCollectionAsList(ALTERNATE_NAMES))
        .containsExactlyInAnyOrder("3", "8", "9", "16", "19", "20", "21");
    assertThat(result.getCollectionAsList("residencies"))
        .containsExactlyInAnyOrder("10", "11", "12", "17", "18");
    assertThat(result.getCollectionAsList("identifications"))
        .containsExactlyInAnyOrder("3", "20");
    assertThat(result.getCollectionAsList("residentialAddresses"))
        .containsExactlyInAnyOrder("13");
  }

  @Test
  public void properValueGroupFive_filledClass() {
    GnsParty result = RecordParser
        .parse(SYSTEM_ID, CHAR_SEP, GROUP_FIVE_FMT_NAME, GROUP_FIVE_VALUE);

    assertThat(result.getSourceSystemIdentifier()).isEqualTo("ICDD");
    assertThat(result.getCustomerIdentificationNo())
        .isEqualTo("1008000330_ABC00349762_803f3b8b-2a15-439d-8ab6-1585756ad5f4");
    assertValuesEqual(result, MapBuilder.from(
        "sourceSystemIdentifier", "ICDD",
        "customerIdentificationNo", "1008000330_ABC00349762_803f3b8b-2a15-439d-8ab6-1585756ad5f4",
        "clientType", "U",
        "fullLegalName", "Armir TAPTA",
        "alternateName1", "1",
        "alternateName2", "2",
        "registOrResidentialAddress",
        "APPARTMENT 5/A WEST, SAVOY COURT,150 ROBINSON ROAD,MIDLEVELS,SINGAPOUR",
        "nationalityAll", "IN",
        "identificationType1", "type1",
        "identificationNumber1", "number1",
        "dateOfBirthOrRegis", "1985-09-24",
        "bookingLocation", "HK",
        "cddRiskRating", "M",
        "clientLegalEntityType", "Private Company",
        "clientSegment", "Middle Market",
        "clientSubSegment", "Middle Markets",
        "customerStatus", "ACTIVE",
        "lastCddApprovedDate", "2015-05-13",
        "linkedCustomerIdentNo", "13526370_ABC00349762_1",
        "partyType", "RELATED PARTY",
        "pepStatus", "U",
        "relatedPartyType", "|Authorised Signatory|Beneficial Owner|Director|",
        "rmCodeCddOwner", "1257742",
        "changeIndicator", "F",
        "dateTimeStamp", "2011-04-10 14:40:35.107",
        "alternateId1", "number2",
        "alternateId2", "number3",
        "identifications", asList("number1", "number2", "number3"),
        NAME, "Armir TAPTA",
        ALTERNATE_NAMES, asList("Armir TAPTA", "1", "2")));
    assertNullValues(result, asList(
        "fullTradingName",
        "legalParentGroupName",
        "executiveManagementNames",
        "supplimentaryCardName",
        "alternateName3",
        "alternateNameRest",
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
        "estCountryOfHeadOffice",
        "estCountryOfParentCompany",
        "identificationType2",
        "identificationNumber2",
        "identificationType3",
        "identificationNumber3",
        "identificationType4",
        "identificationNumber4",
        "identificationTypeRest",
        "identificationNumberRest",
        "countryOfBirthOrRegist",
        "homauthGovtCntryName",
        "nameOfStockExchange",
        "nameOfAuthority",
        "businessNature",
        "classOfBeneficiary",
        "accountRelaProfClsdDate",
        "natureRelaHomeStateAuth",
        "ownershipStatusOfClient",
        "relationshPrimaryCardholder",
        "rmLocationCddOwner",
        "staffAccountIdentifier",
        "gender",
        "countryOfEmployment",
        "departmentName",
        "departmentId",
        "casaFlag",
        "bankCd",
        "alternateId1Description",
        "alternateId2Description",
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
  }

  @Test
  public void givenRecord_getRecordInformation() {
    RecordInformation recordInformation =
        RecordParser.getRecordInformation(CHAR_SEP, RECORD);

    assertThat(recordInformation.getNumberOfColumns()).isEqualTo(50);
    assertThat(recordInformation.getSourceSystemIdentifier()).isEqualTo("EBBS");
  }
}
