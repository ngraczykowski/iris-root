package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GnsPartyIdentificationsTest {

  private final Fixtures fixtures = new Fixtures();

  @Test
  void emptyDocumentNumbersTest() {
    assertPassportNumbers(fixtures.emptyDocument).isEmpty();
    assertNationalIds(fixtures.emptyDocument).isEmpty();
  }

  private static ListAssert<String> assertPassportNumbers(GnsPartyIdentifications testCase) {
    return assertThat(testCase.collectPassportNumbers());
  }

  private static ListAssert<String> assertNationalIds(GnsPartyIdentifications testCase) {
    return assertThat(testCase.collectNationalIds());
  }

  @Test
  void allKnownPassportTypesTest() {
    assertPassportNumbers(fixtures.allKnownPassportTypes)
        .containsExactlyInAnyOrder(fixtures.allKnownPassportTypesValues);
  }

  @Test
  void passportNumbersGroupFiveTest() {
    assertPassportNumbers(fixtures.allKnownPassportTypes)
        .containsExactlyInAnyOrder(fixtures.allKnownPassportTypesValues);
  }

  @Test
  void passportTypeLowerCaseTest() {
    assertPassportNumbers(fixtures.passportTypeLowerCase)
        .containsExactlyInAnyOrder(fixtures.passportTypeLowerCaseValue);
  }

  @Test
  void unknownPassportTypeWithPassportWordTest() {
    assertPassportNumbers(fixtures.unknownPassportTypeWithPassportWord)
        .containsExactlyInAnyOrder(fixtures.unknownPassportTypeWithPassportWordValue);
  }

  @Test
  void panNationalIdsTest() {
    assertNationalIds(fixtures.panNationalIdTypes)
        .containsExactlyInAnyOrder(fixtures.panNationalIdTypesValues);
  }

  @Test
  void pakistaniNationalIdsTest() {
    assertNationalIds(fixtures.pakistaniNationalIdTypes)
        .containsExactlyInAnyOrder(fixtures.pakistaniNationalIdTypesValues);
  }

  @Test
  void idCardNationalIdTypesTest() {
    assertNationalIds(fixtures.idCardNationalIdTypes)
        .containsExactlyInAnyOrder(fixtures.idCardNationalIdTypesValues);
  }

  @Test
  void otherNationalIdTypesTest() {
    assertNationalIds(fixtures.otherNationalIdTypes)
        .containsExactlyInAnyOrder(fixtures.otherNationalIdTypesValues);
  }

  private static class Fixtures {

    String passportGovernmentIssuedValue = "passportGovernmentIssued";
    String passportNoWithDotValue = "passportNoWithDot";
    String passportValue = "passport";
    String validTravelDocumentValue = "validTravelDocument";
    String currentPassportValue = "currentPassportValue";

    String[] allKnownPassportTypesValues = {
        passportGovernmentIssuedValue,
        passportNoWithDotValue,
        passportValue,
        validTravelDocumentValue,
        currentPassportValue
    };

    String passportTypeLowerCaseValue = "passportLowerCaseValue";
    String unknownPassportTypeWithPassportWordValue = "typeWithPassportWordValue";

    GnsPartyIdentifications emptyDocument = GnsPartyIdentifications.builder().build();

    GnsPartyIdentifications allKnownPassportTypes = GnsPartyIdentifications.builder()
        .identificationType1("Passport [Government Issued]")
        .identificationNumber1(passportGovernmentIssuedValue)
        .identificationType2("Passport No.")
        .identificationNumber2(passportNoWithDotValue)
        .identificationType3("Passport")
        .identificationNumber3(passportValue)
        .identificationType4("VALID TRAVEL DOCUMENT")
        .identificationNumber4(validTravelDocumentValue)
        .identificationTypeRest("Current Passport")
        .identificationNumberRest(currentPassportValue)
        .build();

    GnsPartyIdentifications passportTypeLowerCase = GnsPartyIdentifications.builder()
        .identificationType1("passport [government issued]")
        .identificationNumber1(passportTypeLowerCaseValue)
        .build();

    GnsPartyIdentifications unknownPassportTypeWithPassportWord =
        GnsPartyIdentifications.builder()
            .identificationType1("blah blah blah passport blah blah blah")
            .identificationNumber1(unknownPassportTypeWithPassportWordValue)
            .build();

    String panValue = "pan";
    String panNoDotValue = "panNoDot";
    String panPermanentAccountNumberCardValue = "panPermanentAccountNumberCard";

    String pakistaniValue = "pak";
    String nicNoDotValue = "nicNoDot";
    String nicPermanentAccountNumberCardValue = "nicPermanentAccountNumberCardValue";
    String pakistaniNationalId = "pakistaniNationalId";
    String pakistaniNationalCardId = "pakistaniNationalCardId";

    String residentIdCardWithPhotoValue = "residentIdCardWithPhoto";
    String nationalIdentityCardValue = "nationalIdentityCard";
    String nationalIdCardWithPhotoValue = "nationalIdCardWithPhoto";

    String aadhaarCardValue = "aadhaarCard";
    String aadhaarDotValue = "aadhaarDot";
    String localIdentityDocumentValue = "localIdentityDocument";
    String currentNricValue = "currentNric";

    String[] panNationalIdTypesValues = {
        panValue, panNoDotValue, panPermanentAccountNumberCardValue
    };

    String[] pakistaniNationalIdTypesValues = {
        pakistaniValue, nicNoDotValue, nicPermanentAccountNumberCardValue, pakistaniNationalId,
        pakistaniNationalCardId
    };

    String[] idCardNationalIdTypesValues = {
        residentIdCardWithPhotoValue, nationalIdentityCardValue, nationalIdCardWithPhotoValue
    };

    String[] otherNationalIdTypesValues = {
        aadhaarCardValue, aadhaarDotValue, localIdentityDocumentValue, currentNricValue
    };

    GnsPartyIdentifications panNationalIdTypes = GnsPartyIdentifications.builder()
        .identificationType1("PAN (Permanent Account Number) Card")
        .identificationNumber1(panPermanentAccountNumberCardValue)
        .identificationType2("PAN No.")
        .identificationNumber2(panNoDotValue)
        .identificationType3("PAN")
        .identificationNumber3(panValue)
        .build();

    GnsPartyIdentifications pakistaniNationalIdTypes = GnsPartyIdentifications.builder()
        .identificationType1("new nic number")
        .identificationNumber1(pakistaniValue)
        .identificationType2("national identity card")
        .identificationNumber2(nicNoDotValue)
        .identificationType3("nid")
        .identificationNumber3(nicPermanentAccountNumberCardValue)
        .identificationType4("national id")
        .identificationNumber4(pakistaniNationalId)
        .identificationTypeRest("National Identification Card")
        .identificationNumberRest(pakistaniNationalCardId)
        .build();

    GnsPartyIdentifications idCardNationalIdTypes = GnsPartyIdentifications.builder()
        .identificationType1("Resident ID Card (with Photo)")
        .identificationNumber1(residentIdCardWithPhotoValue)
        .identificationType2("National Identity Card")
        .identificationNumber2(nationalIdentityCardValue)
        .identificationType3("National ID Card (with Photo)")
        .identificationNumber3(nationalIdCardWithPhotoValue)
        .build();

    GnsPartyIdentifications otherNationalIdTypes = GnsPartyIdentifications.builder()
        .identificationType1("Aadhaar Card")
        .identificationNumber1(aadhaarCardValue)
        .identificationType2("Aadhaar.")
        .identificationNumber2(aadhaarDotValue)
        .identificationType3("LOCAL IDENTITY DOCUMENT")
        .identificationNumber3(localIdentityDocumentValue)
        .identificationType4("Current NRIC")
        .identificationNumber4(currentNricValue)
        .build();
  }
}
