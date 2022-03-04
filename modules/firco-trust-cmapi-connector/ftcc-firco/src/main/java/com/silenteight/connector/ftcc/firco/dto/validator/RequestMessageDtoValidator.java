package com.silenteight.connector.ftcc.firco.dto.validator;


import com.silenteight.connector.ftcc.firco.dto.input.RequestMessageDto;

public interface RequestMessageDtoValidator {

  void validate(
      RequestMessageDto messageDto, Class<? extends ValidationGroup> validationGroup);
}
