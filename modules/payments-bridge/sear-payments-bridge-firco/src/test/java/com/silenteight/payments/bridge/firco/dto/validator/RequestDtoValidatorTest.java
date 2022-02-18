package com.silenteight.payments.bridge.firco.dto.validator;

import com.silenteight.payments.bridge.firco.dto.input.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.*;

class RequestDtoValidatorTest {

  private Validator validator;

  @BeforeEach
  void beforeAll() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @ParameterizedTest
  @CsvSource(value = {
      "null,null",
      "'user',null",
      "null,'password'",
      "'',''",
      "'user',''",
      "'','password'",
  }, nullValues = { "null" })
  void testMinimalAlertDefinitionAllObligatoryFieldsAreCompleted(
      String userLogin, String userPassword) {
    RequestDto requestDto = createRequestDto(userLogin, userPassword);
    var constraintViolations =
        validator.validate(requestDto, MinimalAlertDefinition.class);
    assertThat(constraintViolations).isNotEmpty();
  }

  private RequestDto createRequestDto(String userLogin, String userPassword) {
    RequestDto requestDto = new RequestDto();
    RequestBodyDto requestBodyDto = new RequestBodyDto();
    RequestSendMessageDto requestSendMessageDto = new RequestSendMessageDto();
    CaseManagerAuthenticationDto caseManagerAuthenticationDto =
        new CaseManagerAuthenticationDto(userLogin, userPassword, "");
    List<RequestMessageDto> messages = new ArrayList<>();

    requestSendMessageDto.setAuthentication(caseManagerAuthenticationDto);
    requestSendMessageDto.setMessages(messages);

    requestBodyDto.setSendMessageDto(requestSendMessageDto);

    requestDto.setBody(requestBodyDto);

    return requestDto;
  }

}
