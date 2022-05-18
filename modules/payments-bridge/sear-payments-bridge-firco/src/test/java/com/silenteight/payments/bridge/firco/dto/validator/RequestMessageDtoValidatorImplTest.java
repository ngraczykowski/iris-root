package com.silenteight.payments.bridge.firco.dto.validator;

import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.firco.dto.input.RequestMessageDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestMessageDtoValidatorImplTest {

  private RequestMessageDtoValidator requestMessageDtoValidator;

  @BeforeEach
  void beforeEach() {
    requestMessageDtoValidator = new RequestMessageDtoValidatorImpl();
  }

  @Test
  void throwNullPointerExceptionForNullRequest() {
    assertThrows(
        NullPointerException.class,
        () -> requestMessageDtoValidator.validate(null, null));
  }

  @ParameterizedTest
  @CsvSource({
      "'AMH+QA','{1:F01SCBLQAQ0AXXX0000000000}{2:I103UNILQAQAXXXXN}{3:{121:151a1360-7f68-483c-8ef1-"
          + "7797f7690547}}{4:\n:20:SECSANC001\n:23B:CRED\n:32A:200203USD234'",
      "'STAR0026','{1:F01SCBLINBBAXXX0000000000}{2:I199BOKLNPKAXXXXN}{3:{108:IR36701908280002}}{4"
          + "::20:IR36701908280002'",
      "'MTS','[FIRCOSOFT     X] FMT V1.0 GEN CoreEngine 5.6.4.2.p0\n[UNIT          X] MTS-SCB\n"
          + "[BUSINESS      X] FTR\n[APPLI         X] MTS\n'",
      "'NBP+KE','[FIRCOSOFT     X] FMT V1.0 GEN CoreEngine 5.6.9.2.p0\n[UNIT          X] NBP-KE\n"
          + "[BUSINESS      X] H2H-NBP\n[APPLI         X]'",
      "'',''",
  })
  void testDamagedAlertDefinitionGroupWhereArgumentsAreNotNullAndNothingHappens(
      String applicationCode, String messageData) {

    RequestMessageDto requestMessageDto = createRequestMessageDto(applicationCode, messageData);
    requestMessageDtoValidator.validate(requestMessageDto, CompleteAlertDefinition.class);

  }

  @ParameterizedTest
  @CsvSource(value = {
      "null,'{1:F01SCBLQAQ0AXXX0000000000}{2:I103UNILQAQAXXXXN}{3:{121:151a1360-7f68-483c-8ef1-"
          + "7797f7690547}}{4:\\n:20:SECSANC001\\n:23B:CRED\\n:32A:200203USD234'",
      "'STAR0026',null",
      "null,null",
  }, nullValues = { "null" })
  void testDamagedAlertDefinitionGroupWhereArgumentsAreNullAndExceptionIsThrown(
      String applicationCode, String messageData) {

    RequestMessageDto requestMessageDto = createRequestMessageDto(applicationCode, messageData);
    assertThrows(
        ValidationException.class,
        () -> requestMessageDtoValidator.validate(
            requestMessageDto, CompleteAlertDefinition.class));

  }

  private RequestMessageDto createRequestMessageDto(String applicationCode, String messageData) {
    RequestMessageDto requestMessageDto = new RequestMessageDto();
    AlertMessageDto alertMessageDto = AlertMessageDto.builder()
        .applicationCode(applicationCode)
        .messageData(messageData)
        .build();
    requestMessageDto.setMessage(alertMessageDto);

    return requestMessageDto;
  }

}
