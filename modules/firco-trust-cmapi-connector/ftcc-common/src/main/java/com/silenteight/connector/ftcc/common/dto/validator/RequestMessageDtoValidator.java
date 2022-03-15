package com.silenteight.connector.ftcc.common.dto.validator;


import com.silenteight.connector.ftcc.common.dto.input.RequestMessageDto;

public interface RequestMessageDtoValidator {

  void validate(
      RequestMessageDto messageDto, Class<? extends ValidationGroup> validationGroup);
}
