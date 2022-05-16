package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class SupplementaryInformationHelperTest {

  private static final String CUSTOMER_IDENTIFICATION_NO = "CUSTOMER_IDENTIFICATION_NO";
  private static final String SOURCE_SYSTEM_IDENTIFIER = "SOURCE_SYSTEM_IDENTIFIER";
  private static final String SUPPLEMENTARY_KEY = "supplementaryInformation1";

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
  void shouldValidateSupplementaryInformation1FieldInGnsParty(String value, boolean expected) {
    //given
    gnsParty.add(SUPPLEMENTARY_KEY, value);
    underTest = new SupplementaryInformationHelper(gnsParty);

    //when
    var result = underTest.getChineseNameFromSupplementaryInformation1();

    //then
    assertThat(result.isPresent()).isEqualTo(expected);
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
  void shouldValidateSupplementaryInformation1FieldInScreenableData(
      String value, boolean expected) {
    //given
    ScreenableData data = new ScreenableData();
    data.setSupplementaryInformation1(value);
    underTest = new SupplementaryInformationHelper(data);

    //when
    var result = underTest.getChineseNameFromSupplementaryInformation1();

    //then
    assertThat(result.isPresent()).isEqualTo(expected);
  }
}
