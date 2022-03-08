package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class SupplementaryInformationHelperTest {

  private static final String CUSTOMER_IDENTIFICATION_NO = "CUSTOMER_IDENTIFICATION_NO";
  private static final String SOURCE_SYSTEM_IDENTIFIER = "SOURCE_SYSTEM_IDENTIFIER";
  private static final String SUPPLEMENTARY_KEY = "supplementaryInformation1";
  private static final String SOURCE_SYSTEM_KEY = "sourceSystemIdentifier";
  private static final String BOOKING_LOCATION_KEY = "bookingLocation";
  private static final String SOURCE_SYSTEM_VALUE_CUPD = "CUPD";
  private static final String SOURCE_SYSTEM_VALUE_TNDM = "TNDM";
  private static final String BOOKING_LOCATION_VALUE_CN = "CN";
  private static final String BOOKING_LOCATION_VALUE_TW = "TW";
  private static final String CHINESE_SUPPLEMENTARY_VALUE = "黃波";

  private GnsParty gnsParty;
  private SupplementaryInformationHelper underTest;

  @BeforeEach
  public void setup() {
    gnsParty = GnsParty.create(SOURCE_SYSTEM_IDENTIFIER, CUSTOMER_IDENTIFICATION_NO);
  }

  @ParameterizedTest(name = "\"{0}\" should be {1}")
  @CsvSource({
      ", false",
      "' ', false",
      "13, false",
      "$<#>, false",
      "Tom, false",
      "李XYZ, false",
      "黃波, true" })
  void shouldValidateSupplementaryInformation1Field(String value, boolean expected) {
    //given
    gnsParty.add(SOURCE_SYSTEM_KEY, SOURCE_SYSTEM_VALUE_CUPD);
    gnsParty.add(BOOKING_LOCATION_KEY, BOOKING_LOCATION_VALUE_CN);
    gnsParty.add(SUPPLEMENTARY_KEY, value);
    underTest = new SupplementaryInformationHelper(gnsParty);

    //when
    underTest = new SupplementaryInformationHelper(gnsParty);
    var result = underTest.getChineseNameFromSupplementaryInformation1();

    //then
    assertThat(result.isPresent()).isEqualTo(expected);
  }

  @ParameterizedTest(name = "{0} should be {1}")
  @CsvSource({
      ", false",
      "ABC, false",
      "IBNK, true",
      "EBBS, true",
      "PANDA, true",
      "CUPD, true" })
  public void whenValidSourceSystem_shouldReturnTrue(String value, boolean expected) {
    //given
    gnsParty.add(SOURCE_SYSTEM_KEY, value);
    gnsParty.add(BOOKING_LOCATION_KEY, BOOKING_LOCATION_VALUE_CN);
    gnsParty.add(SUPPLEMENTARY_KEY, CHINESE_SUPPLEMENTARY_VALUE);

    //when
    underTest = new SupplementaryInformationHelper(gnsParty);
    var result = underTest.getChineseNameFromSupplementaryInformation1();

    //then
    assertThat(result.isPresent()).isEqualTo(expected);
  }

  @Test
  void whenSourceSystemIsTndmAndBookingLocationIsTw_shouldReturnTrue() {
    //given
    gnsParty.add(SOURCE_SYSTEM_KEY, SOURCE_SYSTEM_VALUE_TNDM);
    gnsParty.add(BOOKING_LOCATION_KEY, BOOKING_LOCATION_VALUE_TW);
    gnsParty.add(SUPPLEMENTARY_KEY, CHINESE_SUPPLEMENTARY_VALUE);

    //when
    underTest = new SupplementaryInformationHelper(gnsParty);
    var result = underTest.getChineseNameFromSupplementaryInformation1();

    //then
    assertThat(result).isPresent();
  }

  @Test
  void shouldValidate_whenUseScreenableData() {
    //given
    ScreenableData data = new ScreenableData();
    data.setSourceSystemIdentifier(SOURCE_SYSTEM_VALUE_CUPD);
    data.setAmlCountry(BOOKING_LOCATION_VALUE_CN);
    data.setSupplementaryInformation1(CHINESE_SUPPLEMENTARY_VALUE);

    //when
    underTest = new SupplementaryInformationHelper(data);
    var result = underTest.getChineseNameFromSupplementaryInformation1();

    //then
    assertThat(result).isPresent();
  }
}
