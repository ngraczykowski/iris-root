package com.silenteight.payments.bridge.firco.dto.validator;

import com.silenteight.payments.bridge.firco.dto.input.RequestMessageDto;

public interface RequestMessageDtoValidator {

  void validate(
      RequestMessageDto messageDto, Class<? extends ValidationGroup> validationGroup);
}
